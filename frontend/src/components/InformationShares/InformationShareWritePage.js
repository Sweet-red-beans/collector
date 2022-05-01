import React, { useEffect, useState } from 'react';
import {
  CGVarea,
  CGV0,
  CGV1,
  CGV2,
  CGV3,
  CGV4,
  CGV5,
  CGV6,
  CGV7,
  CGV8,
} from '../../cinemas/CGVcinemas';
import {
  LCarea,
  LC0,
  LC1,
  LC2,
  LC3,
  LC4,
  LC5,
  LC6,
  LC7,
} from '../../cinemas/LCcinemas';
import {
  MBarea,
  MB0,
  MB1,
  MB2,
  MB3,
  MB4,
  MB5,
  MB6,
} from '../../cinemas/MBcinemas';
import { CQarea, CQ0, CQ1, CQ2, CQ3, CQ4 } from '../../cinemas/CQcinemas';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useNavigate } from 'react-router';
import { useSelector } from 'react-redux';
import style from '../../css/InformationSharePage/InformationShareWritePage.module.css';
import { INFO_SHARE_WRITE } from '../../Url/API';
import { INFO_SHARE } from '../../Url/Route';

const InformationShareWritePage = () => {
  let navigation = useNavigate();
  //사용자가 작성한 글 서버에 넘기기
  console.log('정보공유 작성 페이지 렌더');
  const cinemaNames = ['영화관', 'CGV', '롯데시네마', '메가박스', '씨네큐'];
  const CGVarray = [CGV0, CGV1, CGV2, CGV3, CGV4, CGV5, CGV6, CGV7, CGV8];
  const LCarray = [LC0, LC1, LC2, LC3, LC4, LC5, LC6, LC7];
  const MBarray = [MB0, MB1, MB2, MB3, MB4, MB5, MB6];
  const CQarray = [CQ0, CQ1, CQ2, CQ3, CQ4];

  //필터에서 선택한 영화사, 지역, 지점 이름
  const [cinemaName, setCinemaName] = useState('영화관');
  const [cinemaArea, setCinemaArea] = useState('지역');
  const [cinemaBranch, setCinemaBranch] = useState('지점');
  //필터에서 선택하면 true로 바뀜
  const [cinemaNameSelected, setCinemaNameSelected] = useState(false);
  const [cinemaAreaSelected, setCinemaAreaSelected] = useState(false);
  const [cinemaBranchSelected, setCinemaBranchSelected] = useState(false);
  //보여줄 array
  const [cinemaAreas, setCinemaAreas] = useState([]);
  const [array, setArray] = useState([]);
  const [cinemaBranches, setCinemaBranches] = useState([]);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  //사진 첨부
  const [imgFile, setImgFile] = useState(null);
  const [imgBase64, setImgBase64] = useState(null);

  useEffect(() => {
    document.documentElement.scrollTop = 0;
  }, []);

  const cinemaNameChange = (e) => {
    if (e.target.value === '영화관') {
      setCinemaNameSelected(false);
      setCinemaAreaSelected(false);
      setCinemaBranchSelected(false);
    } else {
      const selected = e.target.value;
      setCinemaName(selected);
      if (selected === 'CGV') {
        setCinemaAreas(CGVarea);
        setCinemaAreaSelected(false);
        setCinemaBranchSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(CGVarray);
      } else if (selected === '롯데시네마') {
        setCinemaAreas(LCarea);
        setCinemaAreaSelected(false);
        setCinemaBranchSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(LCarray);
      } else if (selected === '메가박스') {
        setCinemaAreas(MBarea);
        setCinemaAreaSelected(false);
        setCinemaBranchSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(MBarray);
      } else if (selected === '씨네큐') {
        setCinemaAreas(CQarea);
        setCinemaAreaSelected(false);
        setCinemaBranchSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(CQarray);
      }
      setCinemaNameSelected(true);
    }
  };

  const cinemaAreaChange = (e) => {
    if (e.target.value === '지역') {
      setCinemaAreaSelected(false);
      setCinemaBranchSelected(false);
      setCinemaBranch('지점');
    } else {
      setCinemaArea(e.target.value);
      setCinemaBranchSelected(false);
      setCinemaAreaSelected(true);
      setCinemaBranch('지점');
      setCinemaBranches(array[cinemaAreas.indexOf(e.target.value) - 1]);
    }
  };

  const cinemaBranchChange = (e) => {
    setCinemaBranch(e.target.value);
    setCinemaBranchSelected(true);
  };

  const contentChange = (e) => {
    // const a = content.replace("\n", <br/>)
    // console.log(a);
    setContent(e.target.value);
  };

  //const userId = useSelector(state => state.user.user_id);

  //글쓰기 버튼 눌렀을 때
  const writeClick = () => {
    if (title === '' || content === '') {
      alert('내용을 입력해주세요.');
    } else if (
      cinemaName === '영화관' ||
      cinemaArea === '지역' ||
      cinemaBranch === '지점'
    ) {
      alert('지점까지 선택해주세요.');
    } else {
      const fd = new FormData();

      //이미지 파일 없을 때
      if (imgFile === null) {
        fd.append('user_id', '1');
        fd.append('title', title);
        fd.append('cinema_name', cinemaName);
        fd.append('cinema_area', cinemaArea);
        fd.append('cinema_branch', cinemaBranch);
        fd.append('content', content);

        axios
          .post(INFO_SHARE_WRITE, fd, {
            withCredentials: true,
            headers: {
              'Content-Type': `multipart/form-data; `,
            },
          })
          .then((response) => {
            if (response.data.result) {
              alert('게시글이 성공적으로 작성되었습니다.');
              navigation(INFO_SHARE);
              return;
            } else {
              alert('게시글 작성을 실패했습니다.');
            }
          })
          .catch((error) => {
            if (error.response.status === 401) {
              alert('로그인을 먼저 해주세요');
            }
          });
      }
      //이미지 파일 있을 때
      else {
        fd.append('image_url', imgFile[0]);
        fd.append('user_id', '1');
        fd.append('title', title);
        fd.append('cinema_name', cinemaName);
        fd.append('cinema_area', cinemaArea);
        fd.append('cinema_branch', cinemaBranch);
        fd.append('content', content);

        axios
          .post(INFO_SHARE_WRITE, fd, {
            withCredentials: true,
            headers: {
              'Content-Type': `multipart/form-data; `,
            },
          })
          .then((response) => {
            if (response.data.result) {
              alert('게시글이 성공적으로 작성되었습니다.');
              navigation(INFO_SHARE);
            } else {
              alert('게시글 작성을 실패했습니다.');
            }
          })
          .catch((error) => {
            if (error.response.status === 401) {
              alert('로그인을 먼저 해주세요');
            }
          });
      }
    }
  };

  const titleChange = (e) => {
    setTitle(e.target.value);
  };

  const handleChangeFile = (e) => {
    console.log(e.target.files);
    setImgFile(e.target.files);
  };

  useEffect(() => {
    if (!imgFile) return false;

    const reader = new FileReader();
    reader.onloadend = () => {
      setImgBase64(reader.result);
    };
    reader.readAsDataURL(imgFile[0]);
  }, [imgFile]);

  const previewCancelClick = () => {
    setImgFile(null);
    setImgBase64(null);
  };

  return (
    <>
      <div className={style.whiteBox}>
        <div className={style.titleArea}>
          <input
            type="text"
            value={title}
            onChange={titleChange}
            placeholder="제목"
            maxLength="50"
          />
          <div className={style.filterArea}>
            <div className={style.filter}>
              <select onChange={cinemaNameChange} value={cinemaName}>
                {cinemaNames.map((item) => (
                  <option value={item} key={item}>
                    {item}
                  </option>
                ))}
              </select>
              <span className={style.filterArrow}></span>
            </div>

            {cinemaNameSelected ? (
              <div className={style.filter}>
                <select onChange={cinemaAreaChange} value={cinemaArea}>
                  {cinemaAreas.map((item) => (
                    <option value={item} key={item}>
                      {item}
                    </option>
                  ))}
                </select>
                <span className={style.filterArrow}></span>
              </div>
            ) : null}

            {cinemaAreaSelected ? (
              <div className={style.filter}>
                <select onChange={cinemaBranchChange} value={cinemaBranch}>
                  {cinemaBranches.map((item) => (
                    <option value={item} key={item}>
                      {item}
                    </option>
                  ))}
                </select>
                <span className={style.filterArrow}></span>
              </div>
            ) : null}
          </div>
        </div>

        <div className={style.contentArea}>
          <textarea
            onChange={contentChange}
            value={content}
            placeholder="내용을 입력해주세요"
          />
        </div>

        <div className={style.buttonArea}>
          <label htmlFor="upload_file"></label>
          <input
            type="file"
            onChange={handleChangeFile}
            id="upload_file"
            style={{ display: 'none' }}
          />
          {imgBase64 !== null ? (
            <div>
              <img src={imgBase64} />
              <button onClick={previewCancelClick}></button>
            </div>
          ) : null}
          <button onClick={writeClick}>글쓰기</button>
        </div>
      </div>
    </>
  );
};

export default InformationShareWritePage;
