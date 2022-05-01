import { createStore } from 'redux';
import {
  LOGIN_USER,
  MAIN_EVENTS,
  CINEMA_NAMES,
  MAIN_CINEMA_EVENTS,
  EVENTS,
  EVENT_ISEND,
  EVENT_SORT,
  INFO,
  DM_CREATE,
  MYPAGE_USER,
  MYPAGE_TRANSACTIONS,
  MYPAGE_COMMENTS,
  MYPAGE_EVENTS,
  MYPAGE_POSTS,
  MYPAGE_LIKE_TRANSACTIONS,
  SELECTED_DM,
} from './actions/types';

const reducer = (state = {}, action) => {
  if (action.type === LOGIN_USER) {
    //user : nickname, profileURL이 있음
    return { ...state, user: action.user };
  } else if (action.type === MAIN_EVENTS) {
    //전체 이벤트(배열)
    return { ...state, mainEvents: action.events };
  } else if (action.type === CINEMA_NAMES) {
    //
    return { ...state, cinemaNames: action.cinemaNames };
  } else if (action.type === MAIN_CINEMA_EVENTS) {
    //각 영화사 별 이벤트(배열)
    // if (action.mainCinemaEvents.cinemaName === "CGV") {
    //     return {...state, mainCGVEvents: action.mainCinemaEvents.mainCinemaEvents}
    // }
    // else if(action.mainCinemaEvents.cinemaName === "롯데시네마"){
    //     return {...state, mainLCEvents: action.mainCinemaEvents.mainCinemaEvents}
    // }
    // else if(action.mainCinemaEvents.cinemaName === "메가박스"){
    //     return {...state, mainLMBvents: action.mainCinemaEvents.mainCinemaEvents}
    // }
    // else if(action.mainCinemaEvents.cinemaName === "씨네큐"){
    //     return {...state, mainLCQvents: action.mainCinemaEvents.mainCinemaEvents}
    // }
  } else if (action.type === EVENTS) {
    //이벤트 페이지에서 전체 이벤트 조회
    // cinema_name : string
    // event_id : long
    // thumbnail_url : string
    // title : string
    // start_date : date
    // end_date : date
    // is_like : boolean
    return { ...state, events: action.events };
  } else if (action.type === EVENT_ISEND) {
    return { ...state, eventIsEnd: action.payload };
  } else if (action.type === EVENT_SORT) {
    return { ...state, eventSort: action.payload };
  } else if (action.type === INFO) {
    return { ...state, info: action.info };
  } else if (action.type === DM_CREATE) {
    return { ...state, DMCreate: action.DMCreate };
  } else if (action.type === MYPAGE_USER) {
    return { ...state, mypageUser: action.payload };
  } else if (action.type === MYPAGE_TRANSACTIONS) {
    return { ...state, mypageTransactions: action.payload };
  } else if (action.type === MYPAGE_COMMENTS) {
    return { ...state, mypageComments: action.payload };
  } else if (action.type === MYPAGE_EVENTS) {
    return { ...state, mypageEvents: action.payload };
  } else if (action.type === MYPAGE_POSTS) {
    return { ...state, mypagePosts: action.payload };
  } else if (action.type === MYPAGE_LIKE_TRANSACTIONS) {
    return { ...state, mypageLikeTransactions: action.payload };
  } else if (action.type === SELECTED_DM) {
    return { ...state, selectedRoom: action.payload };
  }
};

const store = createStore(reducer);

export default store;
