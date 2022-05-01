import React, { useCallback, useState, useEffect } from 'react';
import axios from 'axios';
import DMListThumbnail from './DMListThumbnail';
import DMDetail from './DMDetail';
import { useDispatch, useSelector } from 'react-redux';
import { SELECTED_DM } from '../../actions/types';
import { useNavigate } from 'react-router';
import style from '../../css/DMPage/DMList.module.css';

const DMList = ({ DMlist }) => {
  const dispatch = useDispatch();

  const DMListClick = (selectedRoom, e) => {
    dispatch({
      type: SELECTED_DM,
      payload: selectedRoom,
    });
  };

  return (
    <>
      <div className={style.DMListContainer}>
        {DMlist.map((item, index) => (
          <div key={index} onClick={(e) => DMListClick(item, e)}>
            <DMListThumbnail dm={item} />
          </div>
        ))}
      </div>
    </>
  );
};

export default DMList;
