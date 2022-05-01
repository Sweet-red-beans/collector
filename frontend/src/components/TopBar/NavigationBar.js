import React, { useEffect, useReducer, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import style from '../../css/TopBar/NavigationBar.module.css';
import { useNavigate } from 'react-router';
import { Cookies } from 'react-cookie';
import {
  EVENT,
  TRANSACTION,
  INFO_SHARE,
  GENERAL,
  DM,
  MY_PAGE,
  MY_EVENTS,
  ADMINPAGE,
} from '../../Url/Route';

const NavigationBar = () => {
  const cookies = new Cookies();
  const navigation = useNavigate();
  const [hide, setHide] = useState(true);
  const [userId, setUserId] = useState(false);

  const onMouseEnter = () => {
    setHide(false);
  };

  const onMouseLeave = () => {
    setHide(true);
  };

  const eventClick = () => {
    navigation(EVENT);
  };

  const transactionClick = () => {
    navigation(TRANSACTION);
  };

  const imformationShareClick = () => {
    navigation(INFO_SHARE);
  };

  const generalBoardClick = () => {
    navigation(GENERAL);
  };

  const DMClick = () => {
    if (cookies.get('login')) {
      navigation(DM);
    } else {
      alert('로그인을 먼저 해주세요');
    }
  };

  const mypageClick = () => {
    if (cookies.get('login')) {
      const authority = cookies.get('user').authority;
      if (authority === '일반') {
        navigation(MY_PAGE + '/' + MY_EVENTS);
      } else if (authority === '관리자') {
        navigation(ADMINPAGE);
      }
    } else {
      alert('로그인을 먼저 해주세요');
    }
  };

  return (
    <>
      <nav>
        <ul className={style.mainMenu}>
          <li>
            <div onClick={eventClick}>이벤트</div>
          </li>
          <li>
            <div onClick={transactionClick}>대리구매</div>
          </li>
          <li>
            <div onClick={imformationShareClick}>커뮤니티</div>
            <ul className={style.subMenu}>
              <li>
                <div onClick={imformationShareClick}>정보공유</div>
              </li>
              <li>
                <div onClick={generalBoardClick}>자유게시판</div>
              </li>
            </ul>
          </li>
          <li>
            <div onClick={DMClick}>개인메시지</div>
          </li>
          <li>
            <div onClick={mypageClick}>마이페이지</div>
          </li>
        </ul>
      </nav>
    </>
  );
};

export default NavigationBar;
