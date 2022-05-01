import React, { useEffect, useState } from 'react';
import Modal from '../Modals/Modal';
import { useNavigate } from 'react-router';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import login from '../../actions/user_action';
import { LOGIN_USER } from '../../actions/types';
import style from '../../css/TopBar/LogIn.module.css';
import { getCookie, setCookie } from '../../Cookie';
import { Cookies } from 'react-cookie';
import { Link } from 'react-router-dom';
import { KAKAO_AUTH_URL, GOOGLE_AUTH_URL } from '../../Url/Url';
import { SIGN_IN, LOGOUT } from '../../Url/API';
import { SIGN_UP } from '../../Url/Route';

const LogIn = () => {
  const navigation = useNavigate();
  const dispatch = useDispatch();

  const [modalOpen, setModalOpen] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState(false);

  const cookies = new Cookies();

  const openModal = () => {
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
  };

  const SignupClick = () => {
    navigation(SIGN_UP);
    setModalOpen(false);
  };

  const emailChange = (e) => {
    setEmail(e.target.value);
  };

  const passwordChange = (e) => {
    setPassword(e.target.value);
  };

  const LoginClick = (e) => {
    e.preventDefault();
    if (email === '' || password === '') {
      return setLoginError(true);
    } else {
      setLoginError(false);
    }

    const body = {
      email: email,
      password: password,
      method: '일반',
    };

    axios
      .post(SIGN_IN, body, {
        withCredentials: true,
      })
      .then((response) => {
        if (response.data.user_status === '정지') {
          alert('정지된 상태입니다. 관리자에게 문의해주세요.');
          setModalOpen(false);
        } else if (response.data.result) {
          const date = new Date();
          date.setMinutes(date.getMinutes() + 30);
          cookies.set('login', true, { expires: date });
          cookies.set(
            'user',
            {
              authority: response.data.authority,
              porfileImage: response.data.image_url,
              nickname: response.data.nickname,
            },
            { expires: date }
          );

          setModalOpen(false);
          navigation(0);
        } else {
          alert('이메일과 비밀번호를 확인해주세요.');
        }
      })
      .catch((error) => console.log(error));
  };

  const KakaoLoginClick = (e) => {
    window.open(KAKAO_AUTH_URL, '_self');
  };

  const logoutClick = () => {
    axios
      .post(LOGOUT, {}, { withCredentials: true })
      .then((response) => {
        if (response.data.result) {
          cookies.remove('login');
          cookies.remove('user');
          navigation(0);
        } else {
          alert('로그아웃에 실패했습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  const googleLoginClick = () => {
    window.open(GOOGLE_AUTH_URL, '_self');
  };

  return (
    <>
      <div className={style.loginArea}>
        {cookies.get('login') ? (
          <button onClick={logoutClick} className={style.loginButton}>
            로그아웃
          </button>
        ) : (
          <button onClick={openModal} className={style.loginButton}>
            로그인
          </button>
        )}
      </div>

      <Modal open={modalOpen} close={closeModal} header="로그인">
        <form>
          <div className={style.emailArea}>
            <input
              type="text"
              placeholder="example@naver.com"
              onChange={emailChange}
              value={email}
              className={style.inputText}
            />
            {email === '' ? (
              <div className={style.errormessage}>*이메일을 입력해주세요</div>
            ) : null}
          </div>

          <div className={style.passwordArea}>
            <input
              type="password"
              placeholder="********"
              onChange={passwordChange}
              value={password}
              className={style.inputText}
            />
            {password === '' ? (
              <div className={style.errormessage}>*비밀번호를 입력해주세요</div>
            ) : null}
          </div>

          <button onClick={LoginClick} className={style.innerloginButton}>
            로그인
          </button>
          {loginError && (
            <div className={style.errormessage}>
              *이메일과 비밀번호를 입력해주세요
            </div>
          )}
        </form>

        <button
          id="signup"
          onClick={SignupClick}
          className={style.signupButton}>
          회원가입
        </button>
        <div>
          <button onClick={KakaoLoginClick} className={style.kakaologinButton}>
            카카오로 로그인
          </button>
        </div>
        <div>
          <button
            onClick={googleLoginClick}
            className={style.googleloginButton}>
            구글로 로그인
          </button>
        </div>
      </Modal>
    </>
  );
};

export default LogIn;
