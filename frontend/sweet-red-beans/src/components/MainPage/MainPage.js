import React, { useState, useEffect } from "react";
import MainMovieEvents from "./MainMovieEvents";
import { useDispatch } from "react-redux";
import { CINEMA_NAMES } from "../../actions/types";
import axios from "axios";
import mainEvents from "../../actions/main_action";
import {MAIN_EVENTS} from "../../actions/types";
import style from "../../css/MainPage/MainPage.module.css";
import MainPosts from "./MainPosts";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router";


const MainPage = () => {
    const [cinemaNames, setCinemaNames] = useState(["CGV", "롯데시네마", "메가박스", "씨네큐"]);
    const dispatch = useDispatch();
    const [mainVideo, setMainVideo] = useState("");
    const navigation = useNavigate();

    dispatch({
        type: CINEMA_NAMES,
        cinemaNames: cinemaNames,
    });

    useEffect(() => {
        axios.get("http://localhost:8080/main/video", {
            withCredentials: true,
        })
        .then(response => {
            setMainVideo(response.data.src)
        })
        .catch(error => console.log(error))
    }, [])

    const arrowClick = () => {
        navigation('/event');
    }


    return(
        <>
        <div className={style.videoArea}>
            <video src={mainVideo} className={style.video} controls></video>
        </div>
        <div className={style.eventsText}>
            EVENTS
            <div className={style.arrow} onClick={arrowClick}></div>
        </div>
        {cinemaNames.map((item, index) => <div key={index} className={style.movieThumbnail}><MainMovieEvents cinemaName={item}/></div>)}

        <MainPosts/>
        
        </>
    )

}

export default MainPage;