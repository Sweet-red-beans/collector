import React, { useEffect, useState, useMemo, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import GeneralBoard from './GeneralBoard';
import style from '../../css/GeneralBoardPage/GeneralBoardPage.module.css';

const GeneralBoardPage = () => {
  const dispatch = useDispatch();

  //이제까지 검색한 것
  const [searchWords, setSearchWords] = useState([]);

  //search
  const [search, setSearch] = useState('');
  const sorts = ['제목+내용', '제목', '내용', '작성자'];
  const [sort, setSort] = useState('제목+내용');

  useEffect(() => {
    document.documentElement.scrollTop = 0;
  }, []);

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
            <GeneralBoard searchWords={searchWords} sort={sort} />
          ),
          [searchWords]
        )}
      </div>
    </>
  );
};

export default GeneralBoardPage;
