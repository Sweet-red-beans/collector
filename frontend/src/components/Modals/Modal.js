import React from 'react';
import style from '../../css/Modal/modal.module.css';

const Modal = (props) => {
    // 열기, 닫기, 모달 헤더 텍스트를 부모로부터 받아옴
    const { open, close, header } = props;

    return (
        // 모달이 열릴때 openModal 클래스가 생성된다.
        <div
            className={
                open ? `${style.openModal} ${style.modal}` : style.modal
            }>
            {open ? (
                <section>
                    <div className={style.buttonArea}>
                        <button onClick={close}></button>
                    </div>
                    <header>{header}</header>
                    <main>{props.children}</main>
                    {/* <footer>
            <button className="close" onClick={close}>
              close
            </button>
          </footer> */}
                </section>
            ) : null}
        </div>
    );
};

export default Modal;
