import React, { useEffect, useState } from "react";
import store from "../../../store";
import Pagination from "../../Pagination/Pagination";
import { useSelector } from "react-redux";
import style from "../../../css/MyPage/MyPageDetail/MyComments.module.css";
import { useNavigate } from "react-router";
import { parseDate } from "../../../parseDate/parseDate";

const MyComments = () => {
    const navigation = useNavigate();
    const myComments = useSelector(s => {
        if(s !== undefined) {
            return s.mypageComments
        }
    })
    const [limit, setLimit] = useState(10);
    const [page, setPage] = useState(1);
    const offset = (page - 1) * limit;


    useEffect(() => {
        console.log("내 댓글들 : ", myComments);
        ;
        console.log(new Date("2022-02-21T22:29:34.564").getFullYear());
    }, [myComments])

    const postClick = (postid, e) => {
        navigation('/informationShare/'+postid);
    }

    return (
        <>
        <div className={style.layout}>
            <div className={style.topBar}>
                <div>댓글 내용</div>
                <div>작성시간</div>
            </div>
            {myComments !== undefined ? myComments.slice(offset, offset + limit).map((item, index) => (
                <article key={index} onClick={e => postClick(item.post_id, e)}>
                    <div>{item.comment_content}</div>
                    <div>{parseDate(item.written_date)}</div>
                </article>
            )) : null}
        </div>
        

        <footer className={style.footer}>
            {myComments !== undefined ? 
            <Pagination total={myComments.length}
            limit={limit}
            page={page}
            setPage={setPage}/>
            : null
            }
        </footer>
        </>
    )
}

export default MyComments;