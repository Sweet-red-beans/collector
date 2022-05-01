import React, { Fragment } from 'react';
import LogIn from './LogIn';
import NavigationBar from './NavigationBar';
import { useNavigate } from 'react-router-dom';
import style from '../../css/TopBar/TopBar.module.css';
import { getCookie } from '../../Cookie';
import { useSelector } from 'react-redux';

const TopBar = () => {
  let navigation = useNavigate();

  const titleClick = () => {
    navigation('/');
  };

  return (
    <>
      <div className={style.container}>
        <div className={style.title}>
          <button onClick={titleClick}>콜렉터</button>
        </div>
        <div className={style.navigationBar}>
          <NavigationBar />
        </div>
        <div className={style.loginArea}>
          <LogIn />
        </div>
      </div>
    </>
  );
};

export default TopBar;
