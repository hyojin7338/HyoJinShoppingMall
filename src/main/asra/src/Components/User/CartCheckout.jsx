import React, { useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext.jsx";
import axios from "axios";


const CartCheckout = () => {
    const { user } = useContext(UserContext);
    const location = useLocation();
    const navigate = useNavigate();
    const selectedProducts = location.state?.selectedProducts || [];
    const [checkoutData, setCheckoutData] = useState(null);
    const [availableCoupons, setAvailableCoupons] = useState([]);
    const [selectedCouponId, setSelectedCouponId] = useState(null);
    const [discountAmount, setDiscountAmount] = useState(0);



    // 쿠폰 적용
    const handleApplyCoupon = async () => {
        if (!checkoutData || !selectedCouponId) {
            alert("쿠폰을 선택해주세요.");
            return;
        }

        try {
            const response = await axios.post(
                `http://localhost:8080/cart/${checkoutData.cartId}/apply-coupon/${selectedCouponId}`
            );
            console.log("쿠폰 적용 응답:", response.data);
            alert("쿠폰이 적용되었습니다!");

            // 최신 결제 정보 가져오기
            const updatedCheckoutData = await axios.post(
                `http://localhost:8080/order/cart/${userId}/selected`,
                selectedProducts.map(item => item.cartItemId)
            );
            setCheckoutData(updatedCheckoutData.data);
            setDiscountAmount(updatedCheckoutData.data.discountAmount);
        } catch (error) {
            console.error("쿠폰 적용 실패:", error.response?.data || error);
            alert(error.response?.data || "쿠폰 적용 실패");
        }
    };



    // 쿠폰 취소
    const handleRemoveCoupon = async () => {
        if (!checkoutData) return;

        try {
            await axios.delete(`http://localhost:8080/cart/${checkoutData.cartId}/remove-coupon`);
            alert("쿠폰이 취소되었습니다!");
            setSelectedCouponId(null);

            // 최신 결제 정보 다시 가져오기
            const updatedCheckoutData = await axios.post(
                `http://localhost:8080/order/cart/${userId}/selected`,
                selectedProducts.map(item => item.cartItemId)
            );
            setCheckoutData(updatedCheckoutData.data);
            setDiscountAmount(0);
        } catch (error) {
            alert(error.response?.data || "쿠폰 취소 실패");
        }
    };

    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price ? price.toLocaleString() : "0";
    };

    if (!checkoutData) return <p>로딩 중...</p>;

    return (
        <div className="checkout-container">
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>
            <h2>결제 페이지</h2>

            {/* 선택된 상품 목록 */}
            <div>
                {selectedProducts.map(product => (
                    <div key={product.cartItemId}>
                        <h3>{product.productName}</h3>
                        <p>가격: {formatPrice(product.productPrice)}원</p>
                        <p>수량: {product.qty}</p>
                    </div>
                ))}
            </div>

            {/* 결제 금액 정보 */}
            <div className="payment-summary">
                <h3>결제 금액</h3>
                <p>상품 금액: {formatPrice(checkoutData.totalPrice)}원</p>
                <p>배송비: {formatPrice(checkoutData.shippingFee)}원</p>
                <p>할인 금액: -{formatPrice(discountAmount)}원</p>
                <hr />
                <h3>최종 결제 금액: {formatPrice(checkoutData.finalPrice)}원</h3>
            </div>

            {/* 쿠폰 선택 */}
            <div className="coupon-selection">
                <h3>쿠폰 선택</h3>
                {availableCoupons.length > 0 ? (
                    <select
                        onChange={(e) => setSelectedCouponId(Number(e.target.value))}
                        value={selectedCouponId || ""}
                    >
                        <option value="">쿠폰을 선택하세요</option>
                        {availableCoupons.map(coupon => (
                            <option key={coupon.userCouponId} value={coupon.userCouponId}>
                                {coupon.couponName} - 할인: {formatPrice(coupon.discountAmount)}원
                            </option>
                        ))}
                    </select>
                ) : (
                    <p>사용 가능한 쿠폰이 없습니다.</p>
                )}
                <button onClick={handleApplyCoupon} disabled={!selectedCouponId}>쿠폰 적용</button>
                <button onClick={handleRemoveCoupon} disabled={!selectedCouponId}>쿠폰 취소</button>
            </div>

            {/* 결제 버튼 */}
            <button className="payment-button">결제하기</button>
        </div>
    );
};

export default CartCheckout;
