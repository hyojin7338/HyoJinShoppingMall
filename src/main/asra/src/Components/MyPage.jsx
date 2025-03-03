import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/MyPage.css"; // 스타일 추가

const MyPage = () => {
    const navigate = useNavigate();

    return (
        <div className="mypage-container">
            <h2>마이페이지</h2>
            <div className="mypage-buttons">
                <button onClick={() => navigate("/orders")}>📦 주문 목록</button>
                <button onClick={() => navigate("/order-cancel")}>❌ 주문 취소/반품</button>
                <button onClick={() => navigate("/MyCoupons")}>🎟 내 쿠폰함</button>
                <button onClick={() => navigate("/my-reviews")}>📝 내가 작성한 리뷰</button>
                <button onClick={() => navigate("/my-inquiries")}>❓ 내가 작성한 문의</button>
                <button onClick={() => navigate("/edit-profile")}>⚙ 개인정보 수정</button>
            </div>
        </div>
    );
};

export default MyPage;
