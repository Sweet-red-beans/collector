import React, { useEffect, useReducer, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import styles from "../../css/TopBar/NavigationBar.module.css";
import BottomCategory from "./BottomCategory";
import { useNavigate } from "react-router";
import { Cookies } from "react-cookie";

const NavigationBar = () => {
    const cookies = new Cookies();
    const navigation = useNavigate();
    const [hide, setHide] = useState(true);
    const [userId, setUserId] = useState(false);

    const onMouseEnter = () => {
        setHide(false);
    }

    const onMouseLeave = () => {
        setHide(true);
    }

    const eventClick = () => {
        navigation('/event');
    }

    const transactionClick = () => {
        navigation('/transaction');
    }

    const imformationShareClick = () => {
        navigation('/informationShare');
    }

    const DMClick = () => {
        if(cookies.get("login")) {
            navigation('/DM');
        }
        else {
            alert("로그인을 먼저 해주세요");
        }
    }

    const mypageClick = () => {
        if(cookies.get("login")) {
            const authority = cookies.get("user").authority
            if(authority === "일반") {
                navigation('/mypage');
            }
            else if(authority === "관리자") {
                navigation('/adminpage');
            }
            
        }
        else {
            alert("로그인을 먼저 해주세요");
        }
        
    }


    return (
        <>
        <nav>
        <ul className={styles.mainMenu}>
            <li><div onClick={eventClick}>이벤트</div></li>
            <li><div onClick={transactionClick}>대리구매</div></li>
            <li><div onClick={imformationShareClick}>커뮤니티</div>
                <ul className={styles.subMenu}>
                    <li><div onClick={imformationShareClick}>정보공유</div></li>
                </ul>
            </li>
            <li><div onClick={DMClick}>개인메시지</div></li>
            <li><div onClick={mypageClick}>마이페이지</div></li>
        </ul>
        </nav>
        </>
    );
}

export default NavigationBar;