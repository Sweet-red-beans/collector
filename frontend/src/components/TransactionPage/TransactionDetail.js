import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import { DM_CREATE, SELECTED_DM } from '../../actions/types';
import style from '../../css/TransactionPage/TransactionDetail.module.css';
import Modal from '../Modals/TransactionModal';
import Switch from 'react-switch';
import { parseDate } from '../../parseDate/parseDate';
import user from '../../img/user.png';
import {
  TRANSACTIONS_CHANGE_STATUS,
  TRANSACTIONS,
  DM_API,
  TRANSACTIONS_REPORT,
  TRANSACTIONS_LIKE,
} from '../../Url/API';
import { DM } from '../../Url/Route';

const TransactionDetail = ({ transaction }) => {
  const dispatch = useDispatch();
  const navigation = useNavigate();
  const [status, setStatus] = useState('');
  const [likeStatus, setLikeStatus] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [reportContent, setReportContent] = useState('');
  const [profileImage, setProfileImage] = useState(null);
  const [nickname, setNickname] = useState();

  const serverReqStatus = () => {
    const body = {
      transaction_id: transaction.transaction_id,
      status: status,
    };
    axios
      .post(TRANSACTIONS_CHANGE_STATUS, body, {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response.data);
        if (response.data.result) {
          if (status === '진행중') {
            setStatus('마감');
          } else {
            setStatus('진행중');
          }
        }
      })
      .catch((error) => console.log(error));
  };

  const useConfirm = (message = null, onConfirm, onCancel) => {
    if (!onConfirm || typeof onConfirm !== 'function') {
      return;
    }
    if (onCancel && typeof onCancel !== 'function') {
      return;
    }

    const confirmAction = () => {
      if (window.confirm(message)) {
        onConfirm();
      } else {
        onCancel();
      }
    };

    return confirmAction;
  };

  //삭제버튼 삭제 눌렀을 때
  const deleteConfirm = () => {
    axios
      .delete(TRANSACTIONS, {
        data: {
          user_id: '1',
          transaction_id: transaction.transaction_id,
        },
        withCredentials: true,
      })
      .then((response) => {
        if (response.data.result) {
          navigation(0);
          alert('삭제되었습니다.');
        } else {
          alert('삭제에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  //삭제 취소 버튼 눌렀을 때
  const cancelConfirm = () => console.log('취소');

  //삭제 버튼 클릭했을 때
  const deleteClick = useConfirm(
    '삭제하시겠습니까?',
    deleteConfirm,
    cancelConfirm
  );

  const DMClick = () => {
    if (
      transaction.user_status === '정지' ||
      transaction.user_status === '탈퇴'
    ) {
      alert('정지되거나 탈퇴한 사용자입니다.');
      return;
    }
    if (status === '마감') {
      alert('이미 마감되었습니다.');
      return;
    }
    const body = {
      transaction_id: transaction.transaction_id,
    };
    axios
      .post(DM_API, body, {
        withCredentials: true,
      })
      .then((response) => {
        dispatch({
          type: SELECTED_DM,
          payload: response.data,
        });
        console.log(response.data);
      })
      .then(() => {
        navigation(DM);
        return;
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert('로그인을 먼저 해주세요');
        }
      });
  };

  //좋아요 눌렀을 때
  const likeClick = () => {
    const body = {
      transaction_id: transaction.transaction_id,
    };
    axios
      .post(TRANSACTIONS_LIKE, body, {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response.data);
        if (response.data.result) {
          if (likeStatus) {
            setLikeStatus(false);
          } else {
            setLikeStatus(true);
          }
        } else {
          alert('좋아요 변경에 실패했습니다.');
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert('로그인을 먼저 해주세요');
        }
      });
  };

  useEffect(() => {
    setStatus(transaction.status);
    setLikeStatus(transaction.is_like);
    if (
      transaction.user_status === '정지' ||
      transaction.user_status === '탈퇴'
    ) {
      setNickname('(알수없음)');
      setProfileImage(user);
    } else {
      setNickname(transaction.nickname);
      setProfileImage(transaction.profile_url);
    }

    return () => {
      setStatus(null);
      setLikeStatus(null);
      setNickname(null);
      setProfileImage(null);
    };
  }, [transaction]);

  const openModal = () => {
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
  };

  const reportContentChange = (e) => {
    setReportContent(e.target.value);
  };

  //신고 확인 버튼 눌렀을 때
  const reportConfirm = () => {
    const body = {
      transaction_id: transaction.transaction_id,
      report_content: reportContent,
    };
    axios
      .post(TRANSACTIONS_REPORT, body)
      .then((response) => {
        if (response.data.result) {
          alert('신고되었습니다.');
          setReportContent('');
          setModalOpen(false);
        } else {
          alert('신고에 실패했습니다.');
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert('로그인을 먼저 해주세요');
        }
      });
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

  const statusClick = (checked) => {
    console.log(checked);
    if (!checked) {
      serverReqStatus();
      setStatus('마감');
    } else {
      serverReqStatus();
      setStatus('진행중');
    }
  };

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

      <div className={style.transactionBox}>
        <div className={style.profileArea}>
          <img src={profileImage} />
          <div className={style.nickname}>{nickname}</div>
          <div className={style.reliabilityArea}>{transaction.reliability}</div>
        </div>

        <div className={style.statusArea}>
          {transaction.is_mine ? (
            <label className={style.statusButton}>
              <Switch
                onChange={statusClick}
                checked={status === '진행중' ? true : false}
                onColor="#F32222"
                offColor="#C4C4C4"
                width={85}
                height={30}
                handleDiameter={22}
                uncheckedIcon={<div className={style.doneButton}>마감</div>}
                checkedIcon={
                  <div className={style.proceedingButton}>진행중</div>
                }
              />
            </label>
          ) : (
            <div
              className={status === '진행중' ? style.proceeding : style.done}>
              {status === '진행중' ? <div>진행중</div> : <div>마감</div>}
            </div>
          )}
        </div>

        <div className={style.content}>{transaction.content}</div>

        <div className={style.bottomArea}>
          <div>
            {transaction.is_mine ? (
              <div className={style.deleteArea}>
                <button onClick={deleteClick}></button>
              </div>
            ) : (
              <div className={style.notMineButtonArea}>
                <button onClick={openModal}></button>
                <button onClick={DMClick}></button>
                {likeStatus ? (
                  <button
                    onClick={likeClick}
                    className={style.likeOnButton}></button>
                ) : (
                  <button
                    onClick={likeClick}
                    className={style.likeOffButton}></button>
                )}
              </div>
            )}
          </div>

          <div className={style.date}>
            {parseDate(transaction.written_date)}
          </div>
        </div>
      </div>
    </>
  );
};

export default TransactionDetail;
