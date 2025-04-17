import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../../context/UserContext.jsx";
import "../../styles/MyCoupons.css";
import FooterNav from "./FooterNav.jsx";

const MyCoupons = () => {
    const { user } = useContext(UserContext);
    const [coupons, setCoupons] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            return;
        }

        axios.get(`http://15.164.216.15:8080/coupons/available/${user.userId}`) // API 수정됨 (유저별 쿠폰 조회)
            .then(response => {
                console.log("내 쿠폰 목록:", response.data);
                setCoupons(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("쿠폰 조회 실패:", error);
                setLoading(false);
            });
    }, [user]);

    if (loading) return <p>로딩 중...</p>;
    if (!coupons.length) return <p>보유한 쿠폰이 없습니다.</p>;

    return (
        <div className="coupon-container">
            <h2>내 쿠폰함</h2>
            <div className="coupon-list">
                {coupons.map(coupon => (
                    <div key={coupon.couponId} className="coupon-item">
                        <h3>{coupon.name}</h3>
                        <p>할인율: {coupon.discountValue}%</p>
                        <p>만료일: {new Date(coupon.endDay).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
            <FooterNav/>
        </div>
    );
};

export default MyCoupons;
