import React, { useEffect, useState } from "react";
import store from "../../../store";
import Pagination from "./Pagination";
import { useNavigate } from "react-router";
import { useSelector } from "react-redux";

const MyPosts = () => {
    const navigation = useNavigate();
    
    const myPosts = useSelector(s => {
        if(s !== undefined) {
            return s.mypagePosts
        }
    })

    const [limit, setLimit] = useState(10);
    const [page, setPage] = useState(1);
    const offset = (page - 1) * limit;

    useEffect(() => {
        console.log("내가 쓴 글들 : ", myPosts);
    }, [myPosts])

    const postClick = (postid, e) => {
        navigation('/informationShare/'+postid);
    }

    //날짜 형식 바꾸기
    const parseDate = (written_date) => {
        const d = new Date(written_date);
        const year = d.getFullYear();
        const month = d.getMonth();
        const date = d.getDate();
        const hours = d.getHours();
        const min = d.getMinutes();
        return (
            <div>{year}-{month}-{date}, {hours} : {min}</div>
        )
    }

    return (
        <>
        {myPosts !== undefined ? myPosts.slice(offset, offset + limit).map((item, index) => (
            <article key={index} onClick={e => postClick(item.post_id, e)}>
            {item.title},
            {
                parseDate(item.written_date)
            }
            </article>
        )) : null}

        <footer>
            {myPosts !== undefined ? 
            <Pagination total={myPosts.length}
            limit={limit}
            page={page}
            setPage={setPage}/>
            : null
            }
        </footer>
        </>
    )
}

export default MyPosts;