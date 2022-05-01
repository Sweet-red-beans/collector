import React, { useEffect, useState, useMemo, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
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
import InformationShares from './InformationShares';
import { Link } from 'react-router-dom';
import { INFO } from '../../actions/types';
import axios from 'axios';
import style from '../../css/InformationSharePage/InformationSharePage.module.css';

const InformationSharePage = () => {
  const dispatch = useDispatch();

  const cinemaNames = ['전체', 'CGV', '롯데시네마', '메가박스', '씨네큐'];
  const CGVarray = [CGV0, CGV1, CGV2, CGV3, CGV4, CGV5, CGV6, CGV7, CGV8];
  const LCarray = [LC0, LC1, LC2, LC3, LC4, LC5, LC6, LC7];
  const MBarray = [MB0, MB1, MB2, MB3, MB4, MB5, MB6];
  const CQarray = [CQ0, CQ1, CQ2, CQ3, CQ4];

  //필터에서 선택한 영화사, 지역, 지점 이름
  const [cinemaName, setCinemaName] = useState('전체');
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
  //이제까지 검색한 것
  const [searchWords, setSearchWords] = useState([]);

  //search
  const [search, setSearch] = useState('');

  const sorts = ['제목+내용', '제목', '내용', '작성자'];
  const [sort, setSort] = useState('제목+내용');

  useEffect(() => {
    document.documentElement.scrollTop = 0;
  }, []);

  //영화관을 선택했을 때
  const cinemaNameChange = (e) => {
    if (e.target.value === '전체') {
      setCinemaName('전체');
      setCinemaArea('지역');
      setCinemaBranch('지점');
      setCinemaNameSelected(false);
      setCinemaAreaSelected(false);
      setCinemaBranchSelected(false);
    } else {
      const selected = e.target.value;
      setCinemaName(selected);
      if (selected === 'CGV') {
        setCinemaAreas(CGVarea);
        setCinemaAreaSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(CGVarray);
      } else if (selected === '롯데시네마') {
        setCinemaAreas(LCarea);
        setCinemaAreaSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(LCarray);
      } else if (selected === '메가박스') {
        setCinemaAreas(MBarea);
        setCinemaAreaSelected(false);
        setCinemaBranch('지점');
        setCinemaArea('지역');
        setArray(MBarray);
      } else if (selected === '씨네큐') {
        setCinemaAreas(CQarea);
        setCinemaAreaSelected(false);
        setCinemaArea('지역');
        setCinemaBranch('지점');
        setArray(CQarray);
      }
      setCinemaNameSelected(true);
    }
  };

  //지역을 선택했을 때
  const cinemaAreaChange = (e) => {
    if (e.target.value === '지역') {
      setCinemaArea('지역');
      setCinemaBranch('지점');
      setCinemaAreaSelected(false);
      setCinemaBranchSelected(false);
    } else {
      setCinemaArea(e.target.value);
      setCinemaBranch('지점');
      setCinemaAreaSelected(true);
      //선택한 지역에 따라 해당 지역에 있는 지점들 리스트로 지정해줌
      setCinemaBranches(array[cinemaAreas.indexOf(e.target.value) - 1]);
    }
  };

  //지점 선택했을 때
  const cinemaBranchChange = (e) => {
    if (e.target.value === '지점') {
      setCinemaBranch('지점');
    } else {
      setCinemaBranch(e.target.value);
      setCinemaBranchSelected(true);
    }
  };

  const sortChange = (e) => {
    setSort(e.target.value);
  };

  const searchChange = (e) => {
    setSearch(e.target.value);
  };

  //검색 버튼 클릭했을 때
  const searchClick = () => {
    setSearchWords([...searchWords, search]);
  };

  return (
    <>
      <div className={style.whiteBox}>
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

          <div className={style.filter}>
            <select onChange={sortChange} value={sort}>
              {sorts.map((item) => (
                <option value={item} key={item}>
                  {item}
                </option>
              ))}
            </select>
            <span className={style.filterArrow}></span>
          </div>

          <div className={style.search}>
            <input
              type="text"
              placeholder="검색"
              onChange={searchChange}
              value={search}
              size="5"></input>
            <div className={style.underline}></div>
            <button id="searchButton" onClick={searchClick}></button>
          </div>
        </div>

        {useMemo(
          () => (
            <InformationShares
              searchWords={searchWords}
              sort={sort}
              cinemaName={cinemaName}
              cinemaArea={cinemaArea}
              cinemaBranch={cinemaBranch}
            />
          ),
          [searchWords]
        )}
      </div>
    </>
  );
};

export default InformationSharePage;
