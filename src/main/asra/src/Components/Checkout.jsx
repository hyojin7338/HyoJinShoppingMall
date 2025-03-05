import React, { useContext, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import axios from "axios";
import "../styles/Checkout.css";

const Checkout = () => {
    const { user } = useContext(UserContext);
    const { productId } = useParams();
    const navigate = useNavigate();

    const [checkoutData, setCheckoutData] = useState(null);
    const [selectedCoupon, setSelectedCoupon] = useState(null);
    const [discountAmount, setDiscountAmount] = useState(0);

    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        // ✅ 백엔드에서 구매 전 정보 조회
        axios.get(`http://localhost:8080/product/${user.userId}/${productId}`)
            .then(response => {
                console.log("✅ 구매 전 정보:", response.data);
                setCheckoutData(response.data);
            })
            .catch(error => {
                console.error("🚨 구매 전 정보 조회 실패:", error);
            });
    }, [user, productId, navigate]);

    // 쿠폰 적용 핸들러
    const handleCouponChange = (event) => {
        const couponId = event.target.value ? Number(event.target.value) : null;
        setSelectedCoupon(couponId);

        // 선택한 쿠폰이 있다면 할인 금액 계산
        if (checkoutData) {
            const coupon = checkoutData.availableCoupons.find(c => c.userCouponId === couponId);
            if (coupon) {
                if (coupon.discountType === "PERCENT") {
                    setDiscountAmount((checkoutData.amount * coupon.discountValue) / 100);
                } else {
                    setDiscountAmount(coupon.discountValue);
                }
            } else {
                setDiscountAmount(0);
            }
        }
    };

    // 최종 결제 금액 계산
    const finalPrice = checkoutData ? checkoutData.amount - discountAmount : 0;

    // 결제 처리
    const handlePayment = () => {
        alert("결제가 완료되었습니다!");
        navigate("/"); // 결제 완료 후 홈으로 이동
    };

    if (!checkoutData) return <p>로딩 중...</p>;

    return (
        <div className="checkout-container">
            <h2>주문 확인</h2>

            {/* 유저 정보 */}
            <div className="checkout-card">
                <h3>배송 정보</h3>
                <p><strong>이름:</strong> {checkoutData.name}</p>
                <p><strong>연락처:</strong> {checkoutData.tel}</p>
                <p><strong>주소:</strong> {checkoutData.address}, {checkoutData.region}</p>
            </div>

            {/* 상품 정보 */}
            <div className="checkout-card">
                <h3>상품 정보</h3>
                <div className="product-summary">
                    <img src={checkoutData.mainImgUrl} alt={checkoutData.productName} className="product-image" />
                    <div>
                        <p><strong>{checkoutData.productName}</strong></p>
                        <p>가격: {checkoutData.amount.toLocaleString()}원</p>
                        <p>판매자: {checkoutData.businessName}</p>
                        <p>재고 수량: {checkoutData.cnt}개</p>
                    </div>
                </div>
            </div>

            {/* 쿠폰 적용 */}
            <div className="checkout-card">
                <h3>쿠폰 적용</h3>
                <select className="coupon-select" value={selectedCoupon || ""} onChange={handleCouponChange}>
                    <option value="">쿠폰을 선택하세요</option>
                    {checkoutData.availableCoupons.map(coupon => (
                        <option key={coupon.userCouponId} value={coupon.userCouponId}>
                            {coupon.name} ({coupon.discountValue}{coupon.discountType === "PERCENT" ? "%" : "원"} 할인)
                        </option>
                    ))}
                </select>
            </div>

            {/* 결제 금액 */}
            <div className="checkout-summary">
                <h3>결제 금액</h3>
                <p>상품 금액: {checkoutData.amount.toLocaleString()}원</p>
                <p>할인 금액: -{discountAmount.toLocaleString()}원</p>
                <hr />
                <h3>최종 결제 금액: {finalPrice.toLocaleString()}원</h3>
            </div>

            <button className="checkout-button" onClick={handlePayment}>결제하기</button>
        </div>
    );
};

export default Checkout;
