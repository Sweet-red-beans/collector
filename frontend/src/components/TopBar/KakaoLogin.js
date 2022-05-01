import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';
import axios from 'axios';
import { Cookies } from 'react-cookie';
import { SIGN_IN_KAKAO, SIGN_IN } from '../../Url/API';

const KakaoLogin = () => {
  let navigation = useNavigate();
  let code = new URL(window.location.href).searchParams.get('code');
  const cookies = new Cookies();

  useEffect(async () => {
    const data = await axios.get(SIGN_IN_KAKAO, {
      withCredentials: true,
      params: {
        code: code,
      },
    });

    const data2 = await axios.post(SIGN_IN, data.data, {
      withCredentials: true,
    });

    if (data2.data.result) {
      const date = new Date();
      date.setMinutes(date.getMinutes() + 30);
      cookies.set('login', true);
      navigation(0);
    }
    navigation('/');
  }, []);

  return <></>;
};

export default KakaoLogin;
