import React, {
  useState,
  useCallback,
  useEffect,
  useMemo,
  useRef,
} from 'react';
import * as StompJs from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import SockJsClient from 'react-stomp';
import Stomp from 'stompjs';
import axios from 'axios';
import Modal from '../../components/Modals/TransactionModal';
import style from '../../css/DMPage/DMDetail.module.css';
import { useDispatch, useSelector } from 'react-redux';
import { SELECTED_DM } from '../../actions/types';
import { parseDate } from '../../parseDate/parseDate';
import { Cookies } from 'react-cookie';
import user from '../../img/user.png';
import {
  DM_DETAIL,
  STOMP,
  DM_RELIABILITY,
  DM_REPORT,
  DM_TRANSACTION_COMPLETE,
} from '../../Url/API';

let stompClient = null;

//props를 selectedRoom으로 바꾸고 roomId는 selectedRoom.chat_room_id으로 바꾸기
//transaction_id 값 바꾸기
const DMDetail = ({ selectedRoom }) => {
  const cookies = new Cookies();

  const [modalOpen, setModalOpen] = useState(false);

  const [reportContent, setReportContent] = useState('');
  //거래완료되면 true
  //초기값 selectedRoom.is_complete로 설정하기
  //   const [complete, setComplete] = useState(selectedRoom.is_complete);
  const [complete, setComplete] = useState(false);
  const [imgFile, setImgFile] = useState(null);
  const [imgBase64, setImgBase64] = useState(null);

  const [myNickname, setMyNickname] = useState(cookies.get('user').nickname);
  const [userStatus, setUserStatus] = useState(null);

  const openModal = () => {
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
  };

  //대화 내용, 상대방 것 포함
  const [contents, setContents] = useState([]);
  const [username, setUsername] = useState('');
  //내가 보낸 메시지 내용
  const [message, setMessage] = useState('');

  let socket = new SockJS(STOMP);
  let stompClient = Stomp.over(socket);

  //이제까지 메시지 내역 조회
  useEffect(() => {
    console.log(selectedRoom);
    setContents([]);
    setMessage('');
    if (selectedRoom !== undefined) {
      axios
        .get(DM_DETAIL, {
          withCredentials: true,
          params: {
            room_id: selectedRoom.chat_room_id,
          },
        })
        .then((response) => {
          setContents(response.data.message);
        })
        .catch((error) => console.log(error));
    }

    stompClient.connect({}, () => {
      stompClient.subscribe(
        '/sub/chat/room/' + selectedRoom.chat_room_id,
        (data) => {
          console.log(JSON.parse(data.body));
          const newMessage = JSON.parse(data.body);
          addMessage(newMessage);
        }
      );
    });
    setComplete(selectedRoom.is_complete);
    setUserStatus(selectedRoom.not_mine_user_status);

    return () => {
      if (stompClient != null) {
        stompClient.disconnect();
        setContents(null);
      }
    };
  }, [selectedRoom]);

  //전송 버튼 눌렀을 때
  const sendClick = () => {
    sendMessage(message);
    setMessage('');
  };

  const sendMessage = (message) => {
    const fd = new FormData();

    if (imgFile === null) {
      if (message !== '') {
        stompClient.send(
          '/pub/chat/message',
          {},
          JSON.stringify({
            content: message,
            chat_room_id: selectedRoom.chat_room_id,
          })
        );
      }
    } else {
      stompClient.send(
        '/pub/chat/message',
        {},
        JSON.stringify({
          content: message,
          chat_room_id: selectedRoom.chat_room_id,
          image_url: imgBase64,
        })
      );
      setImgBase64(null);
      setImgFile(null);
    }
  };

  const addMessage = (message) => {
    //상대에게 받아온 메시지를 추가함
    setContents((prev) => [...prev, message]);
  };

  const onChange = useCallback((e) => {
    setMessage(e.target.value);
  }, []);

  //신뢰도 +1 주는 버튼
  const reliabilityPlusClick = () => {
    //신뢰도 1 주기
    if (complete === false) {
      alert('거래완료를 해야 신뢰도를 줄 수 있습니다');
    } else {
      const body = {
        user_id: selectedRoom.not_mine_id,
      };
      axios
        .post(DM_RELIABILITY, body, {
          withCredentials: true,
        })
        .then((response) => {
          if (response.data.result) {
            alert('상대방의 신뢰도가 올랐습니다.');
          } else {
            alert('신뢰도를 올리는 데 실패했습니다.');
          }
        })
        .catch((error) => console.log(error));
    }
  };

  //신고 내용
  const reportContentChange = (e) => {
    setReportContent(e.target.value);
  };

  //신고 확인 버튼 눌렀을 때
  const reportConfirm = () => {
    const body = {
      user_id: '1',
      transaction_id: selectedRoom.transaction_id,
      report_content: reportContent,
    };
    axios
      .post(DM_REPORT, body, {
        withCredentials: true,
      })
      .then((response) => {
        if (response.data.result) {
          alert('신고되었습니다.');
          setReportContent('');
          setModalOpen(false);
        } else {
          alert('신고에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  //삭제 취소 버튼 눌렀을 때
  const cancelConfirm = (e) => {
    console.log('신고 취소');
  };

  //모달창에서 신고하기 버튼 눌렀을 때
  const reportClick = (e) => {
    e.preventDefault();
    if (reportContent === '') {
      alert('신고 내용을 입력해주세요');
    } else {
      if (window.confirm('정말 신고하시겠습니까?')) {
        reportConfirm();
      } else {
        cancelConfirm();
      }
    }
  };

  //거래완료 버튼 눌렀을 때
  const completeClick = () => {
    const body = {
      transaction_id: selectedRoom.transaction_id,
    };
    axios
      .post(DM_TRANSACTION_COMPLETE, body, {
        withCredentials: true,
      })
      .then((response) => {
        if (response.data.result) {
          setComplete(true);
        } else {
          alert('거래완료에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    // 메시지가 오면 스크롤 맨 아래로 내려주기
    let messagebox = document.querySelector('#messagebox');
    messagebox.scrollTop = messagebox.scrollHeight;
  }, [contents, imgBase64]);

  const fileChange = (e) => {
    setImgFile(e.target.files);
  };

  const previewCancelClick = () => {
    setImgFile(null);
    setImgBase64(null);
  };

  useEffect(() => {
    if (!imgFile) return false;

    const reader = new FileReader();
    reader.onloadend = () => {
      setImgBase64(reader.result);
    };
    reader.readAsDataURL(imgFile[0]);
  }, [imgFile]);

  return (
    <>
      <Modal open={modalOpen} close={closeModal} header="신고하기">
        <form className={style.modal}>
          <div>신고사유를 적어주세요</div>
          <div>
            <textarea
              value={reportContent}
              onChange={reportContentChange}></textarea>
          </div>
          <button onClick={reportClick}>신고하기</button>
        </form>
      </Modal>

      <div className={style.chatcontainer}>
        <div className={style.topBar}></div>
        <div className={style.notMyArea}>
          <img
            src={
              userStatus === '정지' || userStatus === '탈퇴'
                ? user
                : selectedRoom.not_mine_profile_url
            }
          />
          <div>
            {userStatus === '정지' || userStatus === '탈퇴'
              ? '(알수없음)'
              : selectedRoom.not_mine_nickname}
          </div>
          <div>{selectedRoom.not_mine_reliability}</div>
          <div>
            <button onClick={reliabilityPlusClick}>신뢰도 주기</button>
            <button onClick={completeClick}>거래완료</button>
            <button onClick={openModal}>신고하기</button>
          </div>
        </div>
        {useMemo(
          () => (
            <div
              id="messagebox"
              className={!complete ? style.messagebox : style.messagebox2}>
              {contents.map((message, index) => (
                <div
                  key={index}
                  className={
                    message.nickname === myNickname
                      ? style.myMessage
                      : style.notMyMessage
                  }>
                  {message.image_url === null || message.image_url === '' ? (
                    <>
                      <div>
                        {message.nickname === myNickname
                          ? parseDate(message.written_date)
                          : null}
                      </div>
                      <div>{message.content}</div>
                      <div>
                        {!(message.nickname === myNickname)
                          ? parseDate(message.written_date)
                          : null}
                      </div>
                    </>
                  ) : (
                    <>
                      <div>
                        {message.nickname === myNickname
                          ? parseDate(message.written_date)
                          : null}
                      </div>
                      <div>
                        <div>{message.content}</div>
                        <div>
                          <img src={message.image_url} />
                        </div>
                      </div>
                      <div>
                        {!(message.nickname === myNickname)
                          ? parseDate(message.written_date)
                          : null}
                      </div>
                    </>
                  )}
                </div>
              ))}
              <div className={style.preview}>
                {imgBase64 !== null ? (
                  <div>
                    <img src={imgBase64} />
                    <button onClick={previewCancelClick}></button>
                  </div>
                ) : null}
              </div>
            </div>
          ),
          [contents, imgBase64, myNickname]
        )}

        {complete ? (
          <div className={style.complete}>거래가 완료되었습니다.</div>
        ) : null}

        <div className={style.writeArea}>
          <label htmlFor="upload_file"></label>
          <input
            type="file"
            onChange={fileChange}
            id="upload_file"
            style={{ display: 'none' }}
          />
          <input
            type="text"
            value={message}
            onChange={onChange}
            name={message}
          />
          <button onClick={sendClick}></button>
        </div>
      </div>
    </>
  );
};

export default DMDetail;
