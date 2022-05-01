import React, { useEffect, useState } from 'react';
import store from '../../../store';
import TransactionDetail from '../../TransactionPage/TransactionDetail';
import Pagination from '../../Pagination/Pagination';
import { useSelector } from 'react-redux';
import style from '../../../css/MyPage/MyPageDetail/MyLikeTransactions.module.css';

const MyLikeTransactions = () => {
  const myLikeTransactions = useSelector((s) => {
    if (s !== undefined) {
      return s.mypageLikeTransactions;
    }
  });

  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  return (
    <>
      <div className={style.container}>
        {myLikeTransactions !== undefined
          ? myLikeTransactions
              .slice(offset, offset + limit)
              .map((item, index) => (
                <article key={index}>
                  <TransactionDetail transaction={item} />
                </article>
              ))
          : null}
      </div>

      <footer className={style.footer}>
        {myLikeTransactions !== undefined ? (
          <Pagination
            total={myLikeTransactions.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </footer>
    </>
  );
};

export default MyLikeTransactions;
