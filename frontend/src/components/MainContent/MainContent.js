import React from 'react';
import SignUp from '../SignUp/SignUp';
import MainPage from '../MainPage/MainPage';
import { Route, Routes } from 'react-router-dom';
import EventDetailPage from '../EventPage/EventDetailPage';
import EventPage from '../EventPage/EventPage';
import TopBar from '../TopBar/TopBar';
import InformationSharePage from '../InformationShares/InformationSharePage';
import InformationShareWritePage from '../InformationShares/InformationShareWritePage';
import InformationShareDetailPage from '../InformationShares/InformationShareDetailPage';
import GeneralBoardPage from '../GeneralBoardPage/GeneralBoardPage';
import GeneralBoardWritePage from '../GeneralBoardPage/GeneralBoardWritePage';
import GeneralBoardDetailPage from '../GeneralBoardPage/GeneralBoardDetailPage';
import TransactionPage from '../TransactionPage/TransactionPage';
import DMPage from '../DMPage/DMPage';
import MyPageAdmin from '../MyPage/MyPageAdmin';
import MyPageNormal from '../MyPage/MyPageNormal';
import KakaoLogin from '../TopBar/KakaoLogin';
import GoogleLogin from '../TopBar/GoogleLogin';
import MyEvents from '../MyPage/MyPageDetail/MyEvents';
import MyTransactions from '../MyPage/MyPageDetail/MyTransactions';
import MyLikeTransactions from '../MyPage/MyPageDetail/MyLikeTransactions';
import MyPosts from '../MyPage/MyPageDetail/MyPosts';
import MyComments from '../MyPage/MyPageDetail/MyComments';
import {
  SIGN_UP,
  EVENT_,
  EVENT_DETAIL,
  INFO_SHARE_,
  INFO_SHARE_WRITE,
  INFO_SHARE_DETAIL,
  GENERAL_,
  GENERAL_WRITE,
  GENERAL_DETAIL,
  TRANSACTION_,
  DM,
  MY_PAGE,
  MY_EVENTS,
  MY_TRANSACTIONS,
  MY_LIKE_TRANSACTIONS,
  MY_POSTS,
  MY_COMMENTS,
  ADMINPAGE,
  SIGN_IN_KAKAO,
  SIGN_IN_GOOGLE,
} from '../../Url/Route';

const MainContent = () => {
  return (
    <>
      <TopBar />
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path={SIGN_UP} element={<SignUp />} />
        <Route path={EVENT_} element={<EventPage />} />
        <Route path={EVENT_DETAIL} element={<EventDetailPage />} />
        <Route path={INFO_SHARE_} element={<InformationSharePage />} />
        <Route
          path={INFO_SHARE_WRITE}
          element={<InformationShareWritePage />}
        />
        <Route
          path={INFO_SHARE_DETAIL}
          element={<InformationShareDetailPage />}
        />
        <Route path={GENERAL_} element={<GeneralBoardPage />} />
        <Route path={GENERAL_WRITE} element={<GeneralBoardWritePage />} />
        <Route path={GENERAL_DETAIL} element={<GeneralBoardDetailPage />} />
        <Route path={TRANSACTION_} element={<TransactionPage />} />
        <Route path={DM} element={<DMPage />} />
        <Route path={MY_PAGE} element={<MyPageNormal />}>
          <Route path={MY_EVENTS} element={<MyEvents />} />
          <Route path={MY_TRANSACTIONS} element={<MyTransactions />} />
          <Route path={MY_LIKE_TRANSACTIONS} element={<MyLikeTransactions />} />
          <Route path={MY_POSTS} element={<MyPosts />} />
          <Route path={MY_COMMENTS} element={<MyComments />} />
        </Route>
        <Route path={ADMINPAGE} element={<MyPageAdmin />} />
        <Route path={SIGN_IN_KAKAO} element={<KakaoLogin />} />
        <Route path={SIGN_IN_GOOGLE} element={<GoogleLogin />} />
      </Routes>
    </>
  );
};

export default MainContent;
