import React, { useState, useEffect } from 'react';
import style from '../../css/MyPage/MyProfile.module.css';
import axios from 'axios';
import { useNavigate } from 'react-router';
import {
  MYPAGE_PROFILE,
  MYPAGE_NICKNAME,
  MYPAGE_DUP_CHECK,
} from '../../Url/API';

const MyProfile = ({
  profileImage,
  nickname,
  reliability,
  nicknameModify,
  setNickname,
  setNicknameModify,
}) => {
  const navigation = useNavigate();
  //   const [profileImage, setProfileImage] = useState('');
  //   const [nickname, setNickname] = useState('');
  //   const [reliability, setReliability] = useState('');
  //프로필 수정
  const [hide, setHide] = useState(true);
  //이미지 불러오기
  const [imgFile, setImgFile] = useState(null);
  //닉네임 변경용
  //   const [nicknameModify, setNicknameModify] = useState('');

  const [preImage, setPreImage] = useState(null);

  //닉네임 중복 확인이 성공적으로 됐을 때 true
  const [nicknameCheck, setNicknameCheck] = useState(false);
  //닉네임 중복 확인 안 됐을 때 false
  const [nicknameError, setNicknameError] = useState(false);

  //프로필 변경 버튼 눌렀을 때
  const profileChangeClick = () => {
    setHide(false);
    setNicknameCheck(false);
    setNicknameError(false);
  };

  //프로필 변경 취소 버튼 눌렀을 때
  const profileCancelClick = () => {
    setHide(true);
  };

  //프로필 변경 완료 버튼 눌렀을 때
  const profileCompleteClick = () => {
    const fd = new FormData();

    //프로필 사진 바뀌었을 때
    if (imgFile !== null) {
      fd.append('file', imgFile[0]);
      fd.append('user_id', '1');
      axios
        .patch(MYPAGE_PROFILE, fd, {
          withCredentials: true,
          headers: {
            'Content-Type': `multipart/form-data; `,
          },
        })
        .then((response) => {
          if (response.data) {
            setImgFile(null);
          }
        })
        .catch((error) => {
          console.log(error);
        });
      setHide(true);
    }

    //닉네임 바뀌었을 때
    if (nickname !== nicknameModify) {
      if (!nicknameCheck) {
        console.log('체크 안함');
        alert('중복체크를 해주세요');
        return setNicknameError(true);
      } else {
        axios
          .patch(
            MYPAGE_NICKNAME,
            {
              nickname: nicknameModify,
            },
            { withCredentials: true }
          )
          .then((response) => {
            if (response.data) {
              console.log('닉네임 변경됨');
              //닉네임 변경되면 변경된 닉네임으로 바꿔줌
              setNickname(nicknameModify);
            }
          })
          .catch((error) => {
            console.log(error);
          });
        setHide(true);
      }
    }

    navigation(0);
  };

  const handleChangeFile = (e) => {
    setImgFile(e.target.files);
  };

  useEffect(() => {
    preview();
    return () => preview();
  });

  const preview = () => {
    if (!imgFile) return false;

    const imgEl = document.querySelector('.preview');
    const reader = new FileReader();
    reader.onloadend = () => {
      //imgEl.style.backgroundImage = `url(${reader.result})`
      setPreImage(reader.result);
      //setProfileImage(reader.result);
    };
    reader.readAsDataURL(imgFile[0]);
  };
  const nicknameModifyChange = (e) => {
    setNicknameModify(e.target.value);
  };
  //중복확인 버튼 누를 때
  const nicknameModifyClick = () => {
    //바뀌었을 때
    if (nickname !== nicknameModify) {
      const userNickname = { nickname: nicknameModify };
      axios
        .post(MYPAGE_DUP_CHECK, userNickname, {
          withCredentials: true,
        })
        .then((response) => {
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
      //바뀌지 않았으면 닉네임 중복 체크 안 해도 됨
      setNicknameCheck(true);
      setNicknameError(false);
    }
  };
  return (
    <>
      <div className={style.profileWhiteBox}>
        {hide ? (
          <div className={style.profileBox}>
            <div>
              <img src={profileImage} />
            </div>
            <div>{nickname}</div>
            <div>
              <button onClick={profileChangeClick}>프로필 수정하기</button>
            </div>

            <div>{reliability}</div>
          </div>
        ) : (
          <div className={style.profileChangeBox}>
            {preImage !== null ? (
              <img src={preImage} />
            ) : (
              <img src={profileImage} />
            )}
            <label htmlFor="upload_file"></label>
            <input
              type="file"
              onChange={handleChangeFile}
              id="upload_file"
              style={{ display: 'none' }}
            />

            <div className={style.nicknameChangeArea} id="nicknameChangeArea">
              <input
                type="text"
                placeholder="닉네임"
                onChange={nicknameModifyChange}
                value={nicknameModify}
                maxLength="15"
              />
              <div></div>
            </div>

            <button
              onClick={nicknameModifyClick}
              className={style.nicknameDupButton}>
              중복 확인하기
            </button>
            {nicknameError && (
              <div className={style.nicknameDupText}>
                닉네임 중복확인해주세요
              </div>
            )}
            <button
              onClick={profileCompleteClick}
              className={style.completeButton}>
              완료
            </button>
            <button onClick={profileCancelClick} className={style.cancelButton}>
              취소
            </button>
          </div>
        )}
      </div>
    </>
  );
};

export default MyProfile;
