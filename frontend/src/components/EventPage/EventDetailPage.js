import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import events from '../../actions/event_action';
import axios from 'axios';
import style from '../../css/EventPage/EventDetailPage.module.css';
import { EVENTS_DETAIL, EVENTS_LIKE } from '../../Url/API';

const EventDetailPage = () => {
  const { id } = useParams();

  const [thisEvent, setThisEvent] = useState(null);

  const [title, setTitle] = useState('');
  const [cinemaName, setCinemaName] = useState('');
  const [imageUrl, setImageUrl] = useState([]);
  const [linkUrl, setLinkUrl] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [likeCount, setLikeCount] = useState(0);

  //좋아요 상태 변경용
  const [status, setStatus] = useState(false);

  useEffect(() => {
    axios
      .get(EVENTS_DETAIL, {
        withCredentials: true,
        params: {
          event_id: id,
        },
      })
      .then((response) => {
        setThisEvent(response.data);
        setStatus(response.data.is_like);
        setTitle(response.data.title);
        setCinemaName(response.data.cinema_name);
        setImageUrl(response.data.detail_image_url);
        setLinkUrl(response.data.link_url);
        setStartDate(response.data.start_date);
        setEndDate(response.data.end_date);
        setLikeCount(response.data.like_count);
        console.log(response.data);
      })
      .catch((error) => console.log(error));

    return () => {
      setThisEvent(null);
      setStatus(null);
      setTitle(null);
      setCinemaName(null);
      setImageUrl(null);
      setLinkUrl(null);
      setStartDate(null);
      setEndDate(null);
      setLikeCount(null);
    };
  }, []);

  const likeClick = () => {
    const body = {
      event_id: thisEvent.event_id,
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
            setLikeCount(likeCount - 1);
          } else {
            setStatus(true);
            setLikeCount(likeCount + 1);
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

  return (
    <>
      <div className={style.whiteBox}>
        <div className={style.titleBox}>
          <div>{title}</div>
          <div>
            {startDate} ~ {endDate}
          </div>
        </div>
        <div className={style.cinemaBox}>
          <div>{cinemaName}</div>
          <div className={style.likeBox}>
            <div>
              {status ? (
                <button
                  onClick={likeClick}
                  className={style.likeOnButton}></button>
              ) : (
                <button
                  onClick={likeClick}
                  className={style.likeOffButton}></button>
              )}
            </div>
            <div>{likeCount}</div>
          </div>
        </div>

        <div>
          {imageUrl.map((item, index) => (
            <img className={style.imageBox} key={index} src={item} />
          ))}
        </div>
        <div className={style.linkBox}>
          <button onClick={() => window.open(linkUrl, '_blank')}>
            자세히 보러 가기
          </button>
        </div>
      </div>
    </>
  );
};

export default EventDetailPage;
