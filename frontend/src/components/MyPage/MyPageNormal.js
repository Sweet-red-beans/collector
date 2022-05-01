import React, { useEffect, useState, useRef, useMemo } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import {
  MYPAGE_USER,
  MYPAGE_TRANSACTIONS,
  MYPAGE_COMMENTS,
  MYPAGE_EVENTS,
  MYPAGE_POSTS,
  MYPAGE_LIKE_TRANSACTIONS,
} from '../../actions/types';
import style from '../../css/MyPage/MyPageNormal.module.css';
import { Outlet, useNavigate } from 'react-router';
import { Route, Routes } from 'react-router-dom';
import MyEvents from './MyPageDetail/MyEvents';
import { Link } from 'react-router-dom';
import MyProfile from './MyProfile';
import { MYPAGE, MYPAGE_WITHDRAWAL } from '../../Url/API';
import {
  MY_PAGE,
  MY_EVENTS,
  MY_TRANSACTIONS,
  MY_LIKE_TRANSACTIONS,
  MY_POSTS,
  MY_COMMENTS,
} from '../../Url/Route';

const MyPageNomal = () => {
  //서버에서 받아온 정보들 저장하기
  const [profileImage, setProfileImage] = useState('');
  const [nickname, setNickname] = useState('');
  const [reliability, setReliability] = useState('');

  //닉네임 변경용
  const [nicknameModify, setNicknameModify] = useState('');

  const dispatch = useDispatch();
  const navigation = useNavigate();

  useEffect(() => {
    axios
      .get(MYPAGE, {
        withCredentials: true,
      })
      .then((response) => {
        setNickname(response.data.user.nickname);
        setNicknameModify(response.data.user.nickname);
        setProfileImage(response.data.user.profile_url);
        setReliability(response.data.user.reliability);
        dispatch({
          type: MYPAGE_USER,
          payload: response.data.user,
        });
        dispatch({
          type: MYPAGE_TRANSACTIONS,
          payload: response.data.writeTransaction,
        });
        dispatch({
          type: MYPAGE_COMMENTS,
          payload: response.data.comment,
        });
        dispatch({
          type: MYPAGE_EVENTS,
          payload: response.data.likeEvent,
        });
        dispatch({
          type: MYPAGE_POSTS,
          payload: response.data.content,
        });
        dispatch({
          type: MYPAGE_LIKE_TRANSACTIONS,
          payload: response.data.likeTransaction,
        });
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert('로그인을 먼저 해주세요');
          navigation('/');
          return;
        }
      });

    return () => {
      setNickname(null);
      setNicknameModify(null);
      setProfileImage(null);
      setReliability(null);
    };
  }, []);

  //메뉴 선택
  const myListClick = (index, e) => {
    navigation(MY_PAGE + '/' + index);
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

  //삭제버튼 눌렀을 때
  const deleteConfirm = () => {
    axios
      .delete(MYPAGE_WITHDRAWAL, {
        withCredentials: true,
        data: {},
      })
      .then((response) => {
        if (response.data.result) {
          alert('탈퇴 되었습니다.');
          //이전 페이지로 이동
          navigation('/');
        } else {
          alert('탈퇴에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  const cancelConfirm = () => console.log('탈퇴 취소');

  const quitClick = useConfirm(
    '정말 탈퇴하시겠습니까?',
    deleteConfirm,
    cancelConfirm
  );

  return (
    <>
      <MyProfile
        profileImage={profileImage}
        nickname={nickname}
        reliability={reliability}
        nicknameModify={nicknameModify}
        setNickname={setNickname}
        setNicknameModify={setNicknameModify}
      />

      <div className={style.detailMenuArea}>
        <nav>
          <ul>
            <li onClick={(e) => myListClick(MY_EVENTS, e)}>
              <button></button>
              <div>관심있는 이벤트</div>
            </li>
            <li onClick={(e) => myListClick(MY_TRANSACTIONS, e)}>
              <button></button>
              <div>내가 쓴 거래</div>
            </li>
            <li onClick={(e) => myListClick(MY_LIKE_TRANSACTIONS, e)}>
              <button></button>
              <div>내가 좋아요 한 거래</div>
            </li>
            <li onClick={(e) => myListClick(MY_POSTS, e)}>
              <button></button>
              <div>내가 쓴 글</div>
            </li>
            <li onClick={(e) => myListClick(MY_COMMENTS, e)}>
              <button></button>
              <div>내가 쓴 댓글</div>
            </li>
          </ul>
        </nav>
      </div>
      <div className={style.detail}>
        <Outlet />
      </div>
      <div className={style.quitArea}>
        <button onClick={quitClick}>탈퇴하기</button>
      </div>
    </>
  );
};

export default MyPageNomal;
