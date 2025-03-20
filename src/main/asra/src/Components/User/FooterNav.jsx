import React from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/FooterNav.css"; // 스타일 파일 추가

const FooterNav = () => {
    const navigate = useNavigate();

    return (
        <div className="footer-nav">
            <div className="button-group">
                <button onClick={() => navigate("/Favorite")}>찜 목록</button>
                <button onClick={() => navigate("/Cart")}>장바구니</button>
                <button onClick={() => navigate("/Main")}>메인화면</button>
                <button onClick={() => navigate("/mypage")}>마이페이지</button>
            </div>
        </div>
    );
};

export default FooterNav;
