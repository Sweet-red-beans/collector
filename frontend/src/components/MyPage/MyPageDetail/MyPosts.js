import React, { useEffect, useState } from 'react';
import store from '../../../store';
import Pagination from '../../Pagination/Pagination';
import { useNavigate } from 'react-router';
import { useSelector } from 'react-redux';
import style from '../../../css/MyPage/MyPageDetail/MyPosts.module.css';
import { parseDate } from '../../../parseDate/parseDate';
import { INFO_SHARE, GENERAL } from '../../../Url/Route';

const MyPosts = () => {
  const navigation = useNavigate();

  const myPosts = useSelector((s) => {
    if (s !== undefined) {
      return s.mypagePosts;
    }
  });

  const [limit, setLimit] = useState(15);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  const postClick = (postid, category, e) => {
    if (category === '정보공유') {
      navigation(INFO_SHARE + '/' + postid);
    } else if (category === '자유') {
      navigation(GENERAL + '/' + postid);
    }
  };

  console.log(myPosts);

  return (
    <>
      <div className={style.layout}>
        <div className={style.topBar}>
          <div>제목</div>
          <div>카테고리</div>
          <div>작성시간</div>
        </div>
        {myPosts !== undefined
          ? myPosts.slice(offset, offset + limit).map((item, index) => (
              <article
                key={index}
                onClick={(e) => postClick(item.post_id, item.category, e)}>
                <div>{item.title}</div>
                <div>{item.category}</div>
                <div>{parseDate(item.written_date)}</div>
              </article>
            ))
          : null}
      </div>

      <footer className={style.footer}>
        {myPosts !== undefined ? (
          <Pagination
            total={myPosts.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </footer>
    </>
  );
};

export default MyPosts;
