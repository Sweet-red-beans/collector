import React, { useCallback, useEffect, useRef, useState } from 'react';
import style from '../../css/Footer/Footer.module.css';
import { ReactComponent as Github } from '../../img/github.svg';
import { ReactComponent as Mail } from '../../img/maildotru.svg';
import { SWEET_RED_BEANS_GITHUB, MEMBER_GITHUB, EMAIL } from '../../Url/Url';

const Footer = () => {
  const members = ['김민지', '이다은', '이지영', '조현정'];
  const [copy, setCopy] = useState(false);
  const [notCopy, setNotCopy] = useState(false);

  const githubClick = useCallback(() => {
    window.open(SWEET_RED_BEANS_GITHUB);
  });

  const mailClick = useCallback(() => {
    //email.classList.toggle("active")
    // setTimeout(() => {addClass()} ,200)

    setCopy(true);
    setTimeout(() => {
      setCopy(false);
    }, 2000);

    if (navigator.clipboard) {
      // (IE는 사용 못하고, 크롬은 66버전 이상일때 사용 가능합니다.)
      navigator.clipboard
        .writeText(EMAIL)
        .then(() => {
          console.log('클립보드에 복사되었습니다.');
        })
        .catch(() => {
          setNotCopy(true);
        });
    } else {
      // 흐름 2.
      if (!document.queryCommandSupported('copy')) {
        setNotCopy(true);
      }

      // 흐름 3.
      const textarea = document.createElement('textarea');
      textarea.value = EMAIL;
      textarea.style.top = 0;
      textarea.style.left = 0;
      textarea.style.position = 'fixed';

      // 흐름 4.
      document.body.appendChild(textarea);
      // focus() -> 사파리 브라우저 서포팅
      textarea.focus();
      // select() -> 사용자가 입력한 내용을 영역을 설정할 때 필요
      textarea.select();
      // 흐름 5.
      document.execCommand('copy');
      // 흐름 6.
      document.body.removeChild(textarea);
      console.log('클립보드에 복사되었습니다.2');
    }
  });

  const memberClick = (index) => {
    window.open(MEMBER_GITHUB[index]);
  };

  return (
    <>
      <footer className={style.footer}>
        <div className={style.github} onClick={githubClick}>
          <Github />
        </div>

        <div
          className={copy ? style.mailArea : style.mailArea}
          onClick={mailClick}>
          <div
            className={
              copy ? `${style.message} ${style.active}` : style.message
            }>
            {EMAIL}
            <div>{notCopy ? '복사 실패!' : '복사!'}</div>
          </div>
          <div className={style.mail}>
            <Mail />
          </div>
        </div>

        <div className={style.team}>스윗레드빈즈</div>

        <div className={style.member}>
          {members.map((member, index) => (
            <button key={index} onClick={() => memberClick(index)}>
              {member}
            </button>
          ))}
        </div>
      </footer>
    </>
  );
};

export default Footer;
