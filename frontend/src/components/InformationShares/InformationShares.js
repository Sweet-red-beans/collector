import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Pagination from '../Pagination/Pagination';
import style from '../../css/InformationSharePage/InformationShares.module.css';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { parseDate } from '../../parseDate/parseDate';
import { useParams } from 'react-router';
import { INFO_SHARE_SEARCH } from '../../Url/API';
import { INFO_SHARE, INFO_SHARE_WRITE } from '../../Url/Route';
const InformationShares = ({
  searchWords,
  sort,
  cinemaName,
  cinemaArea,
  cinemaBranch,
}) => {
  const [infos, setInfos] = useState([]);
  const [limit, setLimit] = useState(15);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  useEffect(() => {
    console.log(searchWords, sort, cinemaName, cinemaArea, cinemaBranch);
    let searchWord = '';
    if (searchWords.length !== 0) {
      searchWord = searchWords[searchWords.length - 1];
      console.log(searchWord);
    }
    if (cinemaName === '전체') {
      axios
        .get(INFO_SHARE_SEARCH, {
          withCredentials: true,
          params: {
            search_word: searchWord,
            sort_name: sort,
          },
        })
        .then((response) => setInfos(response.data))
        .catch((error) => console.log(error));
    } else if (cinemaArea === '지역') {
      axios
        .get(INFO_SHARE_SEARCH, {
          withCredentials: true,
          params: {
            search_word: searchWord,
            cinema_name: cinemaName,
            sort_name: sort,
          },
        })
        .then((response) => setInfos(response.data))
        .catch((error) => console.log(error));
    } else if (cinemaBranch === '지점') {
      axios
        .get(INFO_SHARE_SEARCH, {
          withCredentials: true,
          params: {
            search_word: searchWord,
            cinema_name: cinemaName,
            cinema_area: cinemaArea,
            sort_name: sort,
          },
        })
        .then((response) => setInfos(response.data))
        .catch((error) => console.log(error));
    } else {
      axios
        .get(INFO_SHARE_SEARCH, {
          withCredentials: true,
          params: {
            search_word: searchWord,
            cinema_name: cinemaName,
            cinema_area: cinemaArea,
            cinema_branch: cinemaBranch,
            sort_name: sort,
          },
        })
        .then((response) => setInfos(response.data))
        .catch((error) => console.log(error));
    }

    return () => {
      setInfos(null);
    };
  }, [searchWords]);

  return (
    <div className={style.layout}>
      {/* <label>
            페이지 당 표시할 게시물 수:&nbsp;
            <select
            type="number"
            value={limit}
            onChange={({ target: { value } }) => setLimit(Number(value))}
            >
            <option value="10">10</option>
            <option value="12">12</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
            </select>
        </label> */}

      <div className={style.infoBox}>
        <div className={style.topBar}>
          <div>제목</div>
          <div>글쓴이</div>
          <div>작성시간</div>
          <div>조회수</div>
        </div>
        <main>
          {infos.slice(offset, offset + limit).map((item, index) => (
            <article key={index}>
              <Link
                to={INFO_SHARE + `/${item.post_id}`}
                style={{ textDecoration: 'none' }}>
                <div>{item.title}</div>
              </Link>
              <div>
                {item.user_status === '정지' || item.user_status === '탈퇴'
                  ? '(알수없음)'
                  : item.nickname}
              </div>
              <div>{parseDate(item.written_date)}</div>
              <div>{item.view}</div>
            </article>
          ))}
        </main>
      </div>

      <div className={style.writeButtonArea}>
        <Link to={INFO_SHARE_WRITE}>
          <button>글쓰기</button>
        </Link>
      </div>

      <footer className={style.footer}>
        <Pagination
          total={infos.length}
          limit={limit}
          page={page}
          setPage={setPage}
        />
      </footer>
    </div>
  );
};

export default InformationShares;
