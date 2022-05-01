import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { useParams } from 'react-router';
import store from '../../../store';
import Pagination from '../../Pagination/Pagination';
import { useNavigate } from 'react-router';
import style from '../../../css/MyPage/MyPageDetail/MyEvents.module.css';
import EventMovieThumbnail from '../../EventPage/EventMovieThumbnail';

const MyEvents = () => {
  const navigation = useNavigate();

  const myEvents = useSelector((s) => {
    if (s !== undefined) {
      return s.mypageEvents;
    }
  });

  const [limit, setLimit] = useState(12);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  return (
    <>
      <div className={style.container}>
        {myEvents !== undefined
          ? myEvents.slice(offset, offset + limit).map((item, index) => (
              <article key={index}>
                <EventMovieThumbnail event={item} />
              </article>
            ))
          : null}
      </div>

      <footer className={style.footer}>
        {myEvents !== undefined ? (
          <Pagination
            total={myEvents.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </footer>
    </>
  );
};

export default MyEvents;
