import React from "react";
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export const setCookie = (name, value, option) => {
    return cookies.set(name, value, { ...option });
}

export const getCookie = (name) => {
    return cookies.get(name);
}

// const getCookie = (name) => {

//     // 쿠키 값을 가져옵니다.
    
//     let value = "; "+document.cookie;
    
//     // 키 값을 기준으로 파싱합니다.
    
//     let parts = value.split("; " + name + "=");
    
//     // value를 return!
    
//     if (parts.length === 2) {
    
//     return parts.pop().split(";").shift();
    
//     }
    
// }