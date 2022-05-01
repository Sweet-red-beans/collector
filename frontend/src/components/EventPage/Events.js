import React, { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router';
import { Link } from 'react-router-dom';
import EventDetailPage from './EventDetailPage';
import { useNavigate } from 'react-router';
import EventPage from './EventPage';
import EventMovieThumbnail from './EventMovieThumbnail';
import { useSelector } from 'react-redux';
import axios from 'axios';
import style from '../../css/EventPage/Events.module.css';
import Pagination from '../Pagination/Pagination';
import { EVENTS_SEARCH } from '../../Url/API';

const Events = ({ sort, isEnd, search_word, cinema_name }) => {
  const [limit, setLimit] = useState(12);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  const [events, setEvents] = useState([]);

  useEffect(() => {
    let body = {};
    if (cinema_name[cinema_name.length - 1] === '전체') {
      body = {
        withCredentials: true,
        params: {
          sort_criteria: sort,
          is_end: isEnd,
          search_word: search_word[search_word.length - 1],
        },
      };
    } else {
      body = {
        withCredentials: true,
        params: {
          cinema_name: cinema_name[cinema_name.length - 1],
          sort_criteria: sort,
          is_end: isEnd,
          search_word: search_word[search_word.length - 1],
        },
      };
    }
    axios
      .get(EVENTS_SEARCH, body)
      .then((response) => {
        setEvents(response.data);
      })
      .catch((error) => console.log(error));

    return () => {
      setEvents([]);
    };
  }, [sort, isEnd, search_word, cinema_name]);

  return (
    <>
      <div className={style.events}>
        {events.slice(offset, offset + limit).map((item) => (
          <div key={item.event_id}>
            <EventMovieThumbnail event={item} />
          </div>
        ))}
      </div>

      <footer className={style.footer}>
        <Pagination
          total={events.length}
          limit={limit}
          page={page}
          setPage={setPage}
        />
      </footer>
    </>
  );
};

export default Events;
