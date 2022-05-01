import React, { useEffect, useState } from 'react';
import axios from 'axios';
import mainEvents from '../../actions/main_action';
import { useDispatch, useSelector } from 'react-redux';
import { MAIN_CINEMA_EVENTS } from '../../actions/types';
import style from '../../css/MainPage/MainMovieEvents.module.css';
import Slider from 'react-slick';
import '../../css/slick/slick.css';
import '../../css/slick/slick-theme.css';
import EventMovieThumbnail from '../EventPage/EventMovieThumbnail';
import { MAIN_EVENT_LIMIT } from '../../Url/API';

const MainMovieEvents = ({ cinemaName }) => {
  const dispatch = useDispatch();
  const [events, setEvents] = useState([]);
  const [thisEvents, setThisEvents] = useState([]);
  const filterMovieList = [];

  useEffect(() => {
    axios
      .get(MAIN_EVENT_LIMIT, {
        withCredentials: true,
      })
      .then((response) => {
        setEvents(response.data);
      });
  }, []);

  useEffect(() => {
    console.log(events);
    events.map((item, index) => {
      if (item.cinema_name === cinemaName) {
        filterMovieList.push(item);
      }
    });
    setThisEvents(filterMovieList);
    dispatch({
      type: MAIN_CINEMA_EVENTS,
      mainCinemaEvents: {
        cinemaName: cinemaName,
        mainCinemaEvents: filterMovieList,
      },
    });
  }, [events]);

  const settings = {
    dots: false,
    arrows: true,
    infinite: true,
    speed: 500,
    slidesToShow: 3,
    slidesToScroll: 3,
  };
  return (
    <>
      <div className={style.cinemaName}>
        {cinemaName}
        <div className={style.underline}></div>
      </div>
      <div className={style.movieThumbnails}>
        {thisEvents.length !== 0 ? (
          <Slider {...settings}>
            {thisEvents.map((item, index) => (
              <div key={index}>
                <EventMovieThumbnail event={item} />
              </div>
            ))}
          </Slider>
        ) : (
          <div className={style.nullEvents}>업데이트 된 이벤트가 없어요</div>
        )}
      </div>
    </>
  );
};

export default MainMovieEvents;
