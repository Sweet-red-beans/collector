import React, { useRef, useState, useEffect, useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import DMList from './DMList';
import DMDetail from './DMDetail';
import axios from 'axios';
import { useNavigate } from 'react-router';
import style from '../../css/DMPage/DMPage.module.css';
import { SELECTED_DM } from '../../actions/types';
import { DM_API } from '../../Url/API';

const DMPage = () => {
  const [DMlist, setDMList] = useState([]);
  const selectedRoomId = useSelector((s) => {
    if (s === undefined) {
      return null;
    } else {
      return s.selectedRoom;
    }
  });

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    //DM 목록 조회
    axios
      .get(DM_API, {
        withCredentials: true,
      })
      .then((response) => {
        setDMList(response.data.room_id);
      })
      .catch((error) => {});

    return () => {
      setDMList(null);
    };
  }, []);

  return (
    <>
      <div className={style.dmpage}>
        <div>
          <DMList DMlist={DMlist} />
        </div>

        <div>
          {selectedRoomId !== null ? (
            <DMDetail selectedRoom={selectedRoomId} />
          ) : null}
        </div>
      </div>
    </>
  );
};

export default DMPage;
