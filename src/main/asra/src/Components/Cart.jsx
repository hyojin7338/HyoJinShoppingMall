import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import "../styles/Cart.css";
import FooterNav from "../Components/FooterNav.jsx";

const Cart = () => {
    const { user } = useContext(UserContext);  // 로그인한 유저 정보 가져오기
    const [cart, setCart] = useState(null);
    const [coupons, setCoupons] = useState([]); // 사용 가능한 쿠폰
    const [loading, setLoading] = useState(true);
    const [selectedCouponId, setSelectedCouponId] = useState(null); // 적용된 쿠폰 ID 저장

    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            return;
        }

        axios.get(`http://localhost:8080/cart/${user.userId}`)
            .then(response => {
                console.log("장바구니 데이터:", response.data);
                setCart(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("장바구니 조회 실패:", error);
                setLoading(false);
            });

        // 사용 가능한 쿠폰 가져오기
        axios.get(`http://localhost:8080/coupons/available/${user.userId}`)
            .then(response => setCoupons(response.data))
            .catch(error => console.error("쿠폰 조회 실패:", error));

    }, [user]);

    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price.toLocaleString();  // 1,000 단위로 콤마 추가
    };

    // 쿠폰 적용 핸들러
    const handleApplyCoupon = async (userCouponId) => {
        if (!cart) return;

        try {
            await axios.post(`http://localhost:8080/cart/${cart.cartId}/apply-coupon/${userCouponId}`);

            alert("쿠폰이 적용되었습니다!");

            //  쿠폰 적용 후 최신 장바구니 데이터 다시 가져오기
            axios.get(`http://localhost:8080/cart/${user.userId}`)
                .then(response => setCart(response.data))
                .catch(error => console.error("장바구니 갱신 실패:", error));
        } catch (error) {
            alert(error.response?.data || "쿠폰 적용 실패");
        }
    };

    // 쿠폰 취소 버튼 추가
    const handleRemoveCoupon = async () => {
        if (!cart) return;

        try {
            await axios.delete(`http://localhost:8080/cart/${cart.cartId}/remove-coupon`);
            alert("쿠폰이 취소되었습니다!");

            // 적용된 쿠폰 ID 초기화
            setSelectedCouponId(null);

            // 최신 장바구니 데이터 다시 가져오기
            axios.get(`http://localhost:8080/cart/${user.userId}`)
                .then(response => setCart(response.data))
                .catch(error => console.error("장바구니 갱신 실패:", error));
        } catch (error) {
            alert(error.response?.data || "쿠폰 취소 실패");
        }
    };


    // 특정 상품 삭제
    const handleRemoveItem = async (productId) => {
        if (!cart) return;

        try {
            await axios.delete(`http://localhost:8080/cart/${cart.cartId}/Delete/${productId}`);
            setCart(prevCart => ({
                ...prevCart,
                cartItems: prevCart.cartItems.filter(item => item.productId !== productId)
            }));
            alert("상품이 장바구니에서 삭제되었습니다.");
        } catch (error) {
            alert("상품 삭제 실패");
        }
    };




    if (loading) return <p>로딩 중...</p>;
    if (!cart || cart.cartItems.length === 0) return <p>장바구니가 비어 있습니다.</p>;

    return (
        <div className="cart-container">
            <h2>장바구니</h2>
            {loading ? (
                <p>로딩 중...</p>
            ) : cart && cart.cartItems.length > 0 ? (
                <>
                    <div className="cart-list">
                        {cart.cartItems.map(item => (
                            <div key={item.cartItemId} className="cart-item">
                                <h3>{item.productName}</h3>
                                <p>가격: {formatPrice(item.productPrice)}원</p>
                                <p>수량: {item.qty}</p>
                            </div>
                        ))}
                    </div>

                    {/* 쿠폰 선택 */}
                    <div className="coupon-section">
                        <h3>사용 가능한 쿠폰</h3>
                        {coupons.length > 0 ? (
                            coupons.map(coupon => (
                                <button
                                    key={coupon.couponId}
                                    className="coupon-button"
                                    onClick={() => handleApplyCoupon(coupon.couponId)}
                                    disabled={selectedCouponId !== null} // 쿠폰이 이미 적용되면 다른 쿠폰 비활성화
                                >
                                    {coupon.name} ({coupon.discountValue}
                                    {coupon.discountType === "PERCENT" ? "%" : "원"} 할인)
                                </button>
                            ))
                        ) : (
                            <p>사용 가능한 쿠폰이 없습니다.</p>
                        )}
                    </div>

                    {/* 적용된 쿠폰이 있으면 취소 버튼 표시 */}
                    {selectedCouponId && (
                        <div className="applied-coupon">
                            <p>적용된 쿠폰: {cart.appliedCoupon.name}</p>
                            <button className="cancel-coupon" onClick={handleRemoveCoupon}>쿠폰 취소</button>
                        </div>
                    )}
                    {/*상품 삭제 버튼 추가*/}
                    <div className="cart-list">
                        {cart.cartItems.map(item => (
                            <div key={item.cartItemId} className="cart-item">
                                <h3>{item.productName}</h3>
                                <p>가격: {formatPrice(item.productPrice)}원</p>
                                <p>수량: {item.qty}</p>
                                <button className="remove-button" onClick={() => handleRemoveItem(item.productId)}>삭제</button>
                            </div>
                        ))}
                    </div>

                    <div className="cart-summary">
                        <h3>총 가격: {formatPrice(cart.totalPrice)}원</h3>
                        {cart.discountAmount > 0 && <h3>할인 금액: -{formatPrice(cart.discountAmount)}원</h3>}
                        <h3>배송비 : {formatPrice(cart.shippingFee)}원</h3>
                        <h3>최종 결제 금액: {formatPrice(cart.finalPrice)}원</h3>
                    </div>
                </>
            ) : (
                <p className="empty-cart">장바구니가 비어 있습니다.</p>
            )}



            <FooterNav />
        </div>
    );
};

export default Cart;
