import React from 'react';
import style from '../../css/Pagination.module.css';

function Pagination({ total, limit, page, setPage }) {
    //페이지 수를 만들음, 12개면 2개, 25개면 3개
    const numPages = Math.ceil(total / limit);

    return (
        <>
            <nav className={style.nav}>
                {/*이전버튼(<), page가 1이면 안 보이게 함*/}
                <button
                    onClick={() => setPage(page - 1)}
                    disabled={page === 1}
                    className={style.button}></button>
                {/*Array(numPages)하면 numPages길이의 빈 배열을 만듦 -> fill로 배열을 undefined로 채워줌*/}
                {Array(numPages)
                    .fill()
                    .map((_, i) => (
                        <button
                            className={style.pageButton}
                            key={i + 1}
                            onClick={() => setPage(i + 1)}
                            aria-current={page === i + 1 ? 'page' : null}>
                            {i + 1}
                        </button>
                    ))}
                {/*다음버튼(>), page가 마지막 페이지이면 안 보이게 함*/}
                <button
                    onClick={() => setPage(page + 1)}
                    disabled={page === numPages}
                    className={style.button}></button>
            </nav>
        </>
    );
}

export default Pagination;
