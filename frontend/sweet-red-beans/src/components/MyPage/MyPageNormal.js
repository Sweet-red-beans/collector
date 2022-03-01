import React, { useEffect, useState } from "react";
import MyPageDetail from "./MyPageDetail";
import axios from "axios";
import { useDispatch } from "react-redux";
import { MYPAGE_USER, MYPAGE_TRANSACTIONS, MYPAGE_COMMENTS, MYPAGE_EVENTS, MYPAGE_POSTS, MYPAGE_LIKE_TRANSACTIONS } from "../../actions/types";
import style from "../../css/MyPage/MyPageNormal.module.css";

const MyPageNomal = () => {
    //서버에서 받아온 정보들 저장하기
    const [profileImage, setProfileImage] = useState("");
    const [nickname, setNickname] = useState("");
    const [reliability, setReliability] = useState("");

    //하위 메뉴들
    const [myMenu, setMyMenu] = useState(0); 
    const myList = ["내가 관심있는 이벤트", "내가 쓴 거래", "내가 좋아요 한 거래", "내가 쓴 글", "내가 쓴 댓글"];

    //프로필 수정
    const [hide, setHide] = useState(true);
    //이미지 불러오기
    const [imgFile, setImgFile] = useState(null);
    //닉네임 변경용
    const [nicknameModify, setNicknameModify] = useState("");

    //닉네임 중복 확인이 성공적으로 됐을 때 true
    const [nicknameCheck, setNicknameCheck] = useState(false);
    //닉네임 중복 확인 안 됐을 때 false
    const [nicknameError, setNicknameError] = useState(false);

    const dispatch = useDispatch();

    useEffect(() => {
        axios.post('http://localhost:8080/mypage',{
        params: {
                    user_id: "1",
                }
        }, { withCredentials: true })
        .then(response => {
            setNickname(response.data.user.nickname);
            setNicknameModify(response.data.user.nickname);
            setProfileImage(response.data.user.profile_url);
            setReliability(response.data.user.reliability);
            dispatch({
                type:MYPAGE_USER,
                payload:response.data.user
            })
            dispatch({
                type:MYPAGE_TRANSACTIONS,
                payload:response.data.writeTransaction
            })
            dispatch({
                type:MYPAGE_COMMENTS,
                payload:response.data.comment
            })
            dispatch({
                type:MYPAGE_EVENTS,
                payload:response.data.likeEvent
            })
            dispatch({
                type:MYPAGE_POSTS,
                payload:response.data.content
            })
            dispatch({
                type:MYPAGE_LIKE_TRANSACTIONS,
                payload:response.data.likeTransaction
            })
        })
        .catch(error => console.log(error));
    }, [])

    //메뉴 선택
    const myListClick = (index, e) => {
        setMyMenu(index)
    }

    //프로필 변경 버튼 눌렀을 때
    const profileChangeClick = () => {
        setHide(false);
        setNicknameCheck(false);
        setNicknameError(false);
    }

    //프로필 변경 취소 버튼 눌렀을 때
    const profileCancelClick = () => {
        setHide(true);
    }

    //프로필 변경 완료 버튼 눌렀을 때
    const profileCompleteClick = () => {

        
        const fd = new FormData();

        //프로필 사진 바뀌었을 때
        if(imgFile!==null){
            fd.append("file", imgFile[0])
            fd.append("user_id", "1")
            axios.post('http://localhost:8080/mypage/profile', fd, {
                withCredentials: true,
                headers: {
                    "Content-Type": `multipart/form-data; `,
                }
            })
            .then((response) => {
                if(response.data){
                    setImgFile(null);
                }
            })
            .catch((error) => {
                console.log(error);
            })
            setHide(true);
        }

        //닉네임 바뀌었을 때
        if (nickname !== nicknameModify){
            
            if(!nicknameCheck){
                console.log("체크 안함");
                alert("중복체크를 해주세요")
                return setNicknameError(true);
            }
            else {
                axios.post('http://localhost:8080/mypage/nickname', 
                {
                    nickname: nicknameModify,
                    user_id: "1",
                }, { withCredentials: true })
                .then((response) => {
                if(response.data){
                    console.log("닉네임 변경됨");
                    //닉네임 변경되면 변경된 닉네임으로 바꿔줌
                    setNickname(nicknameModify);
                }
                })
                .catch((error) => {
                    console.log(error);
                })
                setHide(true);
            }
        }
    }
    

    const handleChangeFile = (e) => {
        setImgFile(e.target.files);
    }

    useEffect(() => {
        preview();
        return () => preview();
    })

    const preview = () => {
        if(!imgFile) return false;

        const imgEl = document.querySelector('.preview');
        const reader = new FileReader();
        reader.onloadend = () => {(
            imgEl.style.backgroundImage = `url(${reader.result})`
        )
        setProfileImage(reader.result);
    }
        reader.readAsDataURL(imgFile[0]);
        
    }
    const nicknameModifyChange = (e) => {
        setNicknameModify(e.target.value);
    }

    useEffect(() => {
    }, [profileImage])

    useEffect(() => {
        console.log(nicknameCheck);
    }, [nicknameCheck])

    //중복확인 버튼 누를 때
    const nicknameModifyClick = () => {
        //바뀌었을 때
        if(nickname !== nicknameModify) {
            const userNickname = {nickname: nicknameModify};
            axios.post('http://localhost:8080/mypage/duplicate-check', userNickname, { withCredentials: true })
            .then(response => {
                const data = response.data
                if(data.result === false){
                    alert("사용할 수 있는 닉네임입니다.");
                    setNicknameCheck(true);
                    setNicknameError(false);
                }
                else{
                    alert("사용할 수 없는 닉네임입니다.");
                }
            })
        }
        else {
            //바뀌지 않았으면 닉네임 중복 체크 안 해도 됨
            setNicknameCheck(true);
            setNicknameError(false);
        }
    }

    return (
        <>
        <div className={style.profileBox}>
            {hide? 
            <div>
                <div className={style.profileImageBorder}>
                    <img src={profileImage} className={style.profileImage}/>
                    </div>
                    <div className={style.nickname}>
                    {nickname}
                </div>

                <div className={style.profileChangeButtonArea}>
                <button onClick={profileChangeClick}>프로필 수정하기</button>
                </div>

                <div className={style.reliability}>
                {reliability}
                </div>
            </div>
            : null}

            {
                !hide?
                <div>
                    <div className="preview" style={{width:"100px", height:"100px", backgroundImage:{profileImage}}}></div>
                    <input type="file" onChange={handleChangeFile} multiple="multiple"/>
                    <input type="text" placeholder="닉네임" onChange={nicknameModifyChange} value={nicknameModify}/>
                    <button onClick={nicknameModifyClick}>중복 확인</button>
                    {nicknameError && <div style={{color : 'red'}}>닉네임 중복확인해주세요</div>}
                    <button onClick={profileCompleteClick}>완료</button>
                    <button onClick={profileCancelClick}>취소</button>
                </div>

                : null
            }
        </div>

        
        <div>
            {myList.map((item, index) => (
                <button key={index} onClick={(e) => myListClick(index, e)}>{item}</button>
            ))}
        </div>
        <div>
            <MyPageDetail myList={myList} myMenu={myMenu}/>
        </div>
        <div>
            <button>탈퇴하기</button>
        </div>
        </>
    )
}

export default MyPageNomal;