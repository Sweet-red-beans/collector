import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import style from '../../css/DMPage/DMListThumbnail.module.css';
import { parseDate } from '../../parseDate/parseDate';
import user from '../../img/user.png';

const DMListThumbnail = ({ dm }) => {
  //const cookies = Cookies();
  const dispatch = useDispatch();
  const navigation = useNavigate();
  const [currentRoom, setCurrentRoom] = useState(false);
  const selectedRoomId = useSelector((s) => {
    if (s === undefined) {
      return null;
    } else {
      return s.selectedRoom;
    }
  });

  useEffect(() => {
    if (selectedRoomId !== null) {
      if (dm.chat_room_id === selectedRoomId.chat_room_id) {
        setCurrentRoom(true);
      } else {
        setCurrentRoom(false);
      }
    }
  }, []);
  useEffect(() => {
    if (selectedRoomId !== null) {
      if (dm.chat_room_id === selectedRoomId.chat_room_id) {
        console.log(dm);
        console.log('현재 : ', selectedRoomId);
        setCurrentRoom(true);
      } else {
        setCurrentRoom(false);
      }
    }
  }, [selectedRoomId]);

  return (
    <>
      <div className={currentRoom ? style.currentThumbnail : style.thumbnail}>
        <img
          src={
            dm.not_mine_user_status === '정지' ||
            dm.not_mine_user_status === '탈퇴'
              ? user
              : dm.not_mine_profile_url
          }
        />

        <div className={style.contentArea}>
          <div>
            {dm.not_mine_user_status === '정지' ||
            dm.not_mine_user_status === '탈퇴'
              ? '(알수없음)'
              : dm.not_mine_nickname}
          </div>
          <div>{dm.recent_message}</div>
        </div>
        <div>
          {dm.recent_message === null ? '' : parseDate(dm.recent_message_date)}
        </div>
      </div>
    </>
  );
};

export default DMListThumbnail;
