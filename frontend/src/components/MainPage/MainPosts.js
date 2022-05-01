import React, { useEffect, useState } from 'react';
import axios from 'axios';
import style from '../../css/MainPage/MainPosts.module.css';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router';
import { MAIN_DAILY_COMMUNITY } from '../../Url/API';
import { INFO_SHARE, GENERAL } from '../../Url/Route';

const MainPosts = () => {
  const navigation = useNavigate();
  const [dailyInfoPosts, setDailyInfoPosts] = useState([]);
  const [dailyGeneralPosts, setDailyGeneralPosts] = useState([]);

  useEffect(() => {
    axios

      .get(MAIN_DAILY_COMMUNITY, {
        withCredentials: true,
        params: {
          community_category: '정보공유',
        },
      })
      .then((response) => {
        console.log(response.data);
        setDailyInfoPosts(response.data);
      })
      .catch((error) => console.log(error));

    axios
      .get(MAIN_DAILY_COMMUNITY, {
        withCredentials: true,
        params: {
          community_category: '자유',
        },
      })
      .then((response) => {
        setDailyGeneralPosts(response.data);
      })
      .catch((error) => console.log(error));

    // return () => {
    //   setDailyInfoPosts(null);
    //   setDailyGeneralPosts(null);
    // };
  }, []);

  const writeClick = () => {
    navigation(INFO_SHARE);
  };

  return (
    <>
      <div className={style.posts}>
        {dailyInfoPosts.length !== 0 || dailyGeneralPosts.length !== 0 ? (
          <>
            <ul className={style.dailyInfoPosts}>
              <div className={style.communityName}>정보공유</div>
              <div className={style.arrow} onClick={writeClick}></div>
              {dailyInfoPosts.map((item, index) => (
                <Link
                  to={INFO_SHARE + `/${item.post_id}`}
                  style={{ textDecoration: 'none' }}
                  key={index}>
                  <li>
                    <div className={style.title}>{item.title}</div>
                    <div className={style.countArea}>
                      <div className={style.commentCount}>
                        {item.comments_num}
                      </div>
                      <div className={style.views}>{item.views}</div>
                    </div>
                  </li>
                </Link>
              ))}
            </ul>

            <ul className={style.dailyGeneralPosts}>
              <div className={style.communityName}>자유게시판</div>
              <div className={style.arrow} onClick={writeClick}></div>
              {dailyGeneralPosts.map((item, index) => (
                <Link
                  to={GENERAL + `/${item.post_id}`}
                  style={{ textDecoration: 'none' }}
                  key={index}>
                  <li>
                    <div className={style.title}>{item.title}</div>
                    <div className={style.countArea}>
                      <div className={style.commentCount}>
                        {item.comments_num}
                      </div>
                      <div className={style.views}>{item.views}</div>
                    </div>
                  </li>
                </Link>
              ))}
            </ul>
          </>
        ) : (
          <div className={style.nullMessage}>
            <div>지금 당장 글 쓰러 가기</div>
            <div className={style.arrow} onClick={writeClick}></div>
            <div>내 통장처럼 비어버린 오늘의 글...</div>
          </div>
        )}
      </div>
    </>
  );
};

export default MainPosts;
