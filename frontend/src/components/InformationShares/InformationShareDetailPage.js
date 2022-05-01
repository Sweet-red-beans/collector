import React, { useEffect, useMemo, useState } from 'react';
import { Link, unstable_HistoryRouter } from 'react-router-dom';
import Comment from '../Comment/Comment';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { useNavigate } from 'react-router';
import style from '../../css/InformationSharePage/InformationShareDetailPage.module.css';
import { parseDate } from '../../parseDate/parseDate';
import { INFO_SAHRE_DETAIL, INFO_SAHRE_COMMENT } from '../../Url/API';
import { INFO_SHARE } from '../../Url/Route';

const InformationShareDetailPage = () => {
  let navigation = useNavigate();
  const { postid } = useParams();
  //const userid = useSelector(state => state.user.user_id);
  const [detailInfo, setDetailInfo] = useState({});
  const [comments, setComments] = useState([]);
  const [commentsIsHere, setCommentsIsHere] = useState(false);

  const [commentContent, setCommentContent] = useState('');

  const [views, setViews] = useState(-1);

  //처음 조회
  useEffect(() => {
    document.documentElement.scrollTop = 0;

    axios
      .get(INFO_SAHRE_DETAIL, {
        withCredentials: true,
        params: {
          post_id: postid,
        },
      })
      .then((response) => {
        setDetailInfo(response.data);
        setComments(response.data.comment);
        setViews(response.data.views);
      })
      .catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    console.log('댓글 추가');
    setCommentsIsHere(true);
  }, [comments]);

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

  //삭제버튼 눌렀을 때
  const deleteConfirm = () => {
    axios
      .delete(INFO_SAHRE_DETAIL, {
        withCredentials: true,
        data: {
          post_id: postid,
        },
      })
      .then((response) => {
        if (response.data.result) {
          alert('삭제되었습니다.');
          //이전 페이지로 이동
          navigation(-1);
        } else {
          alert('삭제에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  const cancelConfirm = () => console.log('삭제 취소');

  const deleteClick = useConfirm(
    '삭제하시겠습니까?',
    deleteConfirm,
    cancelConfirm
  );

  const commentContentChange = (e) => {
    setCommentContent(e.target.value);
  };

  //댓글쓰기 버튼 눌렀을 때
  const commentAddClick = () => {
    if (commentContent !== '') {
      setCommentContent('');
      const body = {
        content: commentContent,
        post_id: postid,
      };

      axios
        .post(INFO_SAHRE_COMMENT, body, {
          withCredentials: true,
        })
        .then((response) => {
          if (response.data.result) {
            console.log('댓글 작성됨');
            // navigation(0);
          } else {
            alert('댓글 작성에 실패했습니다.');
          }
        })
        .catch((error) => {
          if (error.response.status === 401) {
            alert('로그인을 먼저 해주세요');
          }
        });
    }
    axios
      .get(INFO_SAHRE_DETAIL, {
        withCredentials: true,
        params: {
          post_id: postid,
        },
      })
      .then((response) => {
        setDetailInfo(response.data);
        setComments(response.data.comment);
      })
      .catch((error) => console.log(error));
  };

  return (
    <>
      <div className={style.whiteBox}>
        <div className={style.detailArea}>
          <div className={style.titleArea}>
            <div>{detailInfo.title}</div>
            <div>
              {detailInfo.cinema_name} &gt; {detailInfo.cinema_area} &gt;{' '}
              {detailInfo.cinema_branch}
            </div>
          </div>

          <div className={style.topBar}>
            <div>
              {detailInfo.user_status === '정지' ||
              detailInfo.user_status === '탈퇴'
                ? '(알수없음)'
                : detailInfo.nickname}
            </div>
            <div>{parseDate(detailInfo.written_date)}</div>

            <div>{views}</div>
          </div>

          <div className={style.contentArea}>
            <div>
              {detailInfo.image_url !== '' ? (
                <img src={detailInfo.image_url} />
              ) : null}
            </div>
            <div>{detailInfo.content}</div>
            <div>
              {detailInfo.is_mine ? (
                <button onClick={deleteClick}>삭제</button>
              ) : null}
            </div>
          </div>

          <div className={style.commentArea}>
            <div>댓글</div>

            {commentsIsHere
              ? comments.map((item, index) => (
                  <div key={index}>
                    <Comment comment={item} />
                  </div>
                ))
              : null}
            <div className={style.commentWriteArea}>
              <input
                type="textarea"
                placeholder="댓글 쓰기"
                value={commentContent}
                onChange={commentContentChange}
              />
              <button onClick={commentAddClick}></button>
            </div>
          </div>
        </div>

        {useMemo(
          () => (
            <div className={style.preButton}>
              <Link to={INFO_SHARE}>
                <button>목록으로 돌아가기</button>
              </Link>
            </div>
          ),
          []
        )}
      </div>
    </>
  );
};

export default InformationShareDetailPage;
