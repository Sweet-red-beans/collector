import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import style from '../../css/EventPage/EventMovieThumbnail.module.css';
import { EVENTS_LIKE } from '../../Url/API';
import { EVENT } from '../../Url/Route';

const EventMovieThumbnail = ({ event }) => {
  //좋아요 상태 변경용
  const [status, setStatus] = useState(event.is_like);

  const likeClick = () => {
    const body = {
      event_id: event.event_id,
    };
    axios
      .post(EVENTS_LIKE, body, {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response.data);
        if (response.data.result) {
          if (status) {
            setStatus(false);
          } else {
            setStatus(true);
          }
        } else {
          alert('좋아요에 실패했습니다.');
        }
      })
      .catch((error) => {
        if (error.response.status === 401) {
          alert('로그인을 먼저 해주세요');
        }
      });
  };

  useEffect(() => {
    if (status === undefined) {
      setStatus(true);
    }
  }, [status]);

  return (
    <>
      <div className={style.container}>
        <Link
          to={EVENT + `/${event.event_id}`}
          style={{ textDecoration: 'none' }}
          className={style.thumbnailArea}>
          <img src={event.thumbnail_url} />
          <div>{event.title}</div>
          <div>
            {event.start_date} ~ {event.end_date}
          </div>
        </Link>

        {status ? (
          <button onClick={likeClick} className={style.likeOnButton}></button>
        ) : (
          <button onClick={likeClick} className={style.likeOffButton}></button>
        )}
      </div>
    </>
  );
};

export default EventMovieThumbnail;
