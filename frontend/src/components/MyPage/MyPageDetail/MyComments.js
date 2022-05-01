import React, { useEffect, useState } from 'react';
import store from '../../../store';
import Pagination from '../../Pagination/Pagination';
import { useSelector } from 'react-redux';
import style from '../../../css/MyPage/MyPageDetail/MyComments.module.css';
import { useNavigate } from 'react-router';
import { parseDate } from '../../../parseDate/parseDate';
import { INFO_SHARE, GENERAL } from '../../../Url/Route';

const MyComments = () => {
  const navigation = useNavigate();
  const myComments = useSelector((s) => {
    if (s !== undefined) {
      console.log(s);
      return s.mypageComments;
    }
  });
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  const postClick = (postid, category, e) => {
    if (category === '정보공유') {
      navigation(INFO_SHARE + '/' + postid);
    } else if (category === '자유') {
      navigation(GENERAL + '/' + postid);
    }
  };

  return (
    <>
      <div className={style.layout}>
        <div className={style.topBar}>
          <div>댓글 내용</div>
          <div>작성시간</div>
        </div>
        {myComments !== undefined
          ? myComments.slice(offset, offset + limit).map((item, index) => (
              <article
                key={index}
                onClick={(e) => postClick(item.post_id, item.category, e)}>
                <div>{item.comment_content}</div>
                <div>{parseDate(item.written_date)}</div>
              </article>
            ))
          : null}
      </div>

      <footer className={style.footer}>
        {myComments !== undefined ? (
          <Pagination
            total={myComments.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </footer>
    </>
  );
};

export default MyComments;
