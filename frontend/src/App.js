import React from 'react';
import MainContent from './components/MainContent/MainContent';
import { Provider } from 'react-redux';
import store from './store';
import style from './css/App/App.module.css';
import Footer from './components/Footer/Footer';

function App() {
  return (
    <div className={style.App}>
        <Provider store={store}>
          <MainContent/>
          <footer className={style.footer}>
            <Footer/>
          </footer>
        </Provider>
    </div>
  );
}

export default App;
