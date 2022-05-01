import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Pagination from '../Pagination/Pagination';
import style from '../../css/GeneralBoardPage/GeneralBoard.module.css';
import axios from 'axios';
import { parseDate } from '../../parseDate/parseDate';
import { GENERAL_SEARCH } from '../../Url/API';
import { GENERAL, GENERAL_WRITE } from '../../Url/Route';

const GeneralBoard = ({ searchWords, sort }) => {
  const [infos, setInfos] = useState([]);
  const [limit, setLimit] = useState(15);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  useEffect(() => {
    let searchWord = '';
    if (searchWords.length !== 0) {
      searchWord = searchWords[searchWords.length - 1];
      console.log(searchWord);
    }

    axios
      .get(GENERAL_SEARCH, {
        withCredentials: true,
        params: {
          search_word: searchWord,
          search_criteria: sort,
        },
      })
      .then((response) => setInfos(response.data))
      .catch((error) => console.log(error));

    return () => {
      setInfos(null);
    };
  }, [searchWords]);

  return (
    <div className={style.layout}>
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
                to={GENERAL + `/${item.post_id}`}
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
        <Link to={GENERAL_WRITE}>
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

export default GeneralBoard;
