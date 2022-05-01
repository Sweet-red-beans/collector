import React, { useEffect, useState } from 'react';
import Report from '../Report/Report';
import axios from 'axios';
import Pagination from '../Pagination/Pagination';
import style from '../../css/MyPage/MyPageAdmin.module.css';
import { Cookies } from 'react-cookie';
import { useNavigate } from 'react-router';
import { MANAGER_REPORT } from '../../Url/API';

const MyPageAdmin = () => {
  const cookies = new Cookies();
  const navigation = useNavigate();

  const [reports, setReports] = useState([]);

  const [limit, setLimit] = useState(15);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  useEffect(() => {
    if (cookies.get('user')) {
      if (cookies.get('user').authority === '일반') {
        alert('접근할 수 없습니다.');
        navigation('/');
        return;
      } else {
        axios
          .get(MANAGER_REPORT, {
            withCredentials: true,
          })
          .then((response) => {
            setReports(response.data.reports);
          })
          .catch((error) => console.log(error));
      }
    } else {
      alert('접근할 수 없습니다.');
      navigation('/');
      return;
    }

    return () => {
      setReports(null);
    };
  }, []);

  return (
    <>
      <div className={style.layout}>
        <div className={style.reportBox}>
          <div className={style.topBar}>
            <div>승인여부</div>
            <div>신고자</div>
            <div>신고 받은 사람</div>
            <div>신고한 시간</div>
          </div>
          <main>
            {reports.slice(offset, offset + limit).map((item, index) => (
              <article key={index}>
                <Report report={item} />
              </article>
            ))}
          </main>
        </div>
        <footer>
          {reports !== undefined ? (
            <Pagination
              total={reports.length}
              limit={limit}
              page={page}
              setPage={setPage}
            />
          ) : null}
        </footer>
      </div>
    </>
  );
};

export default MyPageAdmin;
