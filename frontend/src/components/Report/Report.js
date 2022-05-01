import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import style from '../../css/Report/Report.module.css';
import Modal from '../Modals/Modal';
import { parseDate } from '../../parseDate/parseDate';
import { REPORT } from '../../Url/API';

const Report = ({ report }) => {
  const navigation = useNavigate();
  const [modalOpen, setModalOpen] = useState(false);
  const [content, setContent] = useState('');
  const [date, setDate] = useState('');
  const [nickname, setNickname] = useState('');
  const [reportedNickname, setReportedNickname] = useState('');
  const [isComplete, setIsComplete] = useState(false);

  const openModal = () => {
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
  };

  useEffect(() => {
    setContent(report.report_content);
    setDate(report.written_date);
    setNickname(report.nickname);
    setReportedNickname(report.reported_nickname);
    setIsComplete(report.is_complete);
    console.log(report);
  }, [report]);

  const useConfirm = (message = null, onConfirm, onCancel) => {
    if (!onConfirm || typeof onConfirm !== 'function') {
      return;
    }
    if (onCancel && typeof onCancel !== 'function') {
      return;
    }

    const confirmAction = () => {
      if (window.confirm(message)) {
        onConfirm();
      } else {
        onCancel();
      }
    };

    return confirmAction;
  };

  const confirm = () => {
    axios
      .patch(
        REPORT,
        {
          user_id: report.reported_user_id,
        },
        { withCredentials: true }
      )
      .then((response) => {
        if (response.data) {
          console.log(response.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });
    closeModal();
    navigation(0);
  };

  const cancelConfirm = () => console.log('승인 취소');

  const reportAcceptClick = useConfirm(
    isComplete ? '승인을 취소하시겠습니까?' : '승인하시겠습니까?',
    confirm,
    cancelConfirm
  );

  return (
    <>
      <Modal open={modalOpen} close={closeModal}>
        <form className={style.modal}>
          <div className={style.topArea}>
            <div>신고자 : {nickname}</div>
            <div>{parseDate(date)}</div>
          </div>
          <div>신고 받은 사람 : {reportedNickname}</div>
          <div>{content}</div>
          <button onClick={reportAcceptClick}>
            {isComplete ? '승인취소' : '승인하기'}
          </button>
        </form>
      </Modal>
      <div className={style.main} onClick={openModal}>
        <div className={isComplete ? style.complete : style.incomplete}>
          {isComplete ? '승인' : '미승인'}
        </div>
        <div>{nickname}</div>
        <div>{reportedNickname}</div>
        <div>{parseDate(date)}</div>
      </div>
    </>
  );
};

export default Report;
