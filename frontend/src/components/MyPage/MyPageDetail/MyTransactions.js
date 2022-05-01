import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import store from '../../../store';
import TransactionDetail from '../../TransactionPage/TransactionDetail';
import Pagination from '../../Pagination/Pagination';
import style from '../../../css/MyPage/MyPageDetail/MyTransactions.module.css';

const MyTransactions = () => {
  const myTransactions = useSelector((s) => {
    if (s !== undefined) {
      return s.mypageTransactions;
    }
  });

  const [limit, setLimit] = useState(3);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  useEffect(() => {
    console.log('내가 쓴 거래들 : ', myTransactions);
  }, [myTransactions]);

  return (
    <>
      <div className={style.container}>
        {myTransactions !== undefined
          ? myTransactions.slice(offset, offset + limit).map((item, index) => (
              <article key={index}>
                <TransactionDetail transaction={item} />
              </article>
            ))
          : null}
      </div>

      <footer className={style.footer}>
        {myTransactions !== undefined ? (
          <Pagination
            total={myTransactions.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </footer>
    </>
  );
};

export default MyTransactions;
