import React, { useState, useCallback } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import style from '../../css/SignUp/SignUp.module.css';
import { SIGN_UP, SIGN_UP_DUP_CHECK } from '../../Url/API';

const SignUp = () => {
  const navigation = useNavigate();

  //const dispatch = useDispatch();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordCheck, setPasswordCheck] = useState('');
  const [nickname, setNickname] = useState('');
  //패스워드 안 맞을 때 false
  const [passwordError, setPasswordError] = useState(false);
  //이메일 중복 확인이 성공적으로 됐을 때 true
  const [emailCheck, setEmailCheck] = useState(false);
  //닉네임 중복 확인이 성공적으로 됐을 때 true
  const [nicknameCheck, setNicknameCheck] = useState(false);
  //비밀번호가 형식에 맞게 입력되어야 true
  const [passwordForm, setPasswordForm] = useState(false);
  //비밀번호가 형식에 안 맞으면 true
  const [passwordFormError, setPasswordFormError] = useState(false);
  //이메일 중복 확인 안 됐을 때 false
  const [emailError, setEmailError] = useState(false);
  //닉네임 중복 확인 안 됐을 때 false
  const [nicknameError, setNicknameError] = useState(false);
  //이메일 형식에 맞지 않을 때 false
  const [emailForm, setEmailForm] = useState(false);

  const onChangePasswordChk = useCallback(
    (e) => {
      //비밀번호를 입력할때마다 password 를 검증하는 함수
      setPasswordError(e.target.value !== password);
      setPasswordCheck(e.target.value);
      //password state를 사용하기때문에 password를 넣어준다
    },
    [password, passwordCheck]
  );

  //닉네임 변경됐을 때
  const onChange = (e) => {
    if (e.target.id === 'nickname') {
      setNickname(e.target.value);
    }
  };

  const onEmailChange = (e) => {
    setEmail(e.target.value);
    if (e.target.value !== '' && e.target.value.search('@') !== -1) {
      console.log(emailForm);
      setEmailForm(true);
    } else {
      setEmailForm(false);
    }
  };

  const onPasswordChange = (e) => {
    setPassword(e.target.value);
    setPasswordError(e.target.value !== passwordCheck);
    //유효성 검사
    let num = /[0-9]/;
    let al = /[a-zA-Z]/;
    let char = /[~!@#$%^&*()_+|<>?:{}]/;
    if (
      e.target.value.length >= 8 &&
      num.test(e.target.value) &&
      al.test(e.target.value) &&
      char.test(e.target.value)
    ) {
      setPasswordForm(true);
      setPasswordFormError(false);
      console.log('올바른 비밀번호');
    } else {
      setPasswordForm(false);
    }
  };

  //가입하기 버튼
  const onClick = (e) => {
    e.preventDefault();
    //비밀번호, 비밀번호 확인이 안 같으면 회원가입 안 됨
    if (password !== passwordCheck) {
      return setPasswordError(true);
    }

    //이메일 중복 확인 안 하면 회원가입 안 됨
    if (!emailCheck) {
      return setEmailError(true);
    }

    //닉네임 중복 체크 안 하면 회원가입 안 됨
    if (!nicknameCheck) {
      return setNicknameError(true);
    }

    //비밀번호를 입력하지 않으면 회원가입 안 됨
    if (!passwordForm) {
      return setPasswordFormError(true);
    }

    let body = {
      email: email,
      password: password,
      nickname: nickname,
    };

    // if(emailDup === false || nicknameDup === false){
    //     console.log("로그인 안 됨");
    // }

    axios
      .post(SIGN_UP, body)
      .then((response) => {
        if (response.data.result) {
          alert('회원가입되었습니다!');
          navigation('/');
        } else {
          alert('회원가입할 수 없습니다.');
        }
      })
      .catch((error) => console.log(error));
  };

  //이메일 중복확인 버튼 눌렀을 때
  const clickEmailCheck = (e) => {
    e.preventDefault();
    //이메일 형식에 맞아야 체크했다고 봄
    if (emailForm) {
      console.log('성공적');
      const userEmail = { email: email };
      axios
        .post(SIGN_UP_DUP_CHECK, userEmail)
        .then((response) => {
          console.log(response.data);
          const data = response.data;
          if (data.result === false) {
            setEmailCheck(true);
            setEmailError(false);
            alert('사용할 수 있는 이메일입니다.');
          } else {
            alert('사용할 수 없습니다.');
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
    //이메일 형식에 안 맞을 때
    else {
      console.log('실패');
    }
  };

  const clickNicknameCheck = (e) => {
    e.preventDefault();
    //닉네임이 있어야 체크했다고 봄
    if (nickname !== '') {
      const userNickname = { nickname: nickname };
      axios.post(SIGN_UP_DUP_CHECK, userNickname).then((response) => {
        const data = response.data;
        if (data.result === false) {
          alert('사용할 수 있는 닉네임입니다.');
          setNicknameCheck(true);
          setNicknameError(false);
        } else {
          alert('사용할 수 없는 닉네임입니다.');
        }
      });
    } else {
      console.log('안됨');
    }
  };

  return (
    <>
      <form className={style.whiteBox}>
        <div className={style.signupArea}>
          <div className={style.signUpText}>회원가입</div>

          <div className={style.inputArea}>
            <label>
              <div>이메일</div>
              <input
                className={style.dupInputText}
                id="email"
                type="email"
                placeholder="example@naver.com"
                onChange={onEmailChange}
                value={email}
                maxLength="100"
              />
              <button
                className={style.dupChkButton}
                id="emailCheck"
                onClick={clickEmailCheck}>
                중복 확인하기
              </button>
            </label>
            {email === '' ? (
              <div className={style.errormessage}>* 이메일을 입력해주세요</div>
            ) : null}
            {!emailForm && (
              <div className={style.errormessage}>* 이메일 형식이 아닙니다</div>
            )}
          </div>

          <div className={style.inputArea}>
            <label>
              <div>비밀 번호</div>
              <input
                className={style.inputText}
                id="password"
                type="password"
                placeholder="********"
                onChange={onPasswordChange}
                value={password}
              />
            </label>
            {!passwordForm && (
              <div className={style.errormessage}>
                * 8자 이상, 특수문자, 숫자 포함
              </div>
            )}
          </div>

          <div className={style.inputArea}>
            <label>
              <div>비밀번호 확인</div>
              <input
                className={style.inputText}
                id="passwordCheck"
                type="password"
                placeholder="********"
                onChange={onChangePasswordChk}
                value={passwordCheck}
              />
            </label>
            {passwordError && (
              <div className={style.errormessage}>
                * 비밀번호가 일치하지 않습니다
              </div>
            )}
          </div>

          <div className={style.inputArea}>
            <label>
              <div>닉네임</div>
              <input
                className={style.dupInputText}
                id="nickname"
                type="text"
                placeholder="닉네임"
                onChange={onChange}
                value={nickname}
                maxLength="15"
              />
              <button
                className={style.dupChkButton}
                id="nicknameCheck"
                onClick={clickNicknameCheck}>
                중복 확인하기
              </button>
            </label>
            {nickname === '' ? (
              <div className={style.errormessage}>* 닉네임을 입력해주세요</div>
            ) : null}
          </div>

          <button className={style.joinButton} type="submit" onClick={onClick}>
            가입하기
          </button>
          {emailError && (
            <div className={style.errormessage}>이메일 중복확인해주세요</div>
          )}
          {nicknameError && (
            <div className={style.errormessage}>닉네임 중복확인해주세요</div>
          )}
          {passwordFormError && (
            <div className={style.errormessage}>
              비밀번호 양식을 확인해주세요
            </div>
          )}
        </div>
      </form>
    </>
  );
};

export default SignUp;
