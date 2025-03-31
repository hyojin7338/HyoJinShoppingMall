import React, { useEffect, useState, useContext } from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import { UserContext } from "../../context/UserContext.jsx";
import "../../styles/Cart.css";


const Cart = () => {
    const { user } = useContext(UserContext);  // 로그인한 유저 정보 가져오기
    const navigate = useNavigate();
    const [cart, setCart] = useState(null);
    const [coupons, setCoupons] = useState([]); // 사용 가능한 쿠폰
    const [loading, setLoading] = useState(true);
    const [selectedCouponId, setSelectedCouponId] = useState(null); // 적용된 쿠폰 ID 저장

    // useEffect -> 초기 데이터 로드
    // 사용자가 로그인되어 있는지 확인 -> 로그인하지 않는 경우 alter를 띄운 후 실행 중단(return)
    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            return;
        }

        // 장바구니 데이터 가지고 옴 // API 요청
        axios.get(`http://localhost:8080/cart/${user.userId}`)
            .then(response => {
                // 응답 데이터를 response.data 저장
                console.log("장바구니 데이터:", response.data);
                setCart(response.data);
                // 이미 적용된 쿠폰이 있다면 해당 쿠폰 ID를 selectedCouponId에 저장
                setSelectedCouponId(response.data.appliedCoupon?.couponId || null); // 적용된 쿠폰 ID 동기화
                setLoading(false);
            })
            .catch(error => {
                console.error("장바구니 조회 실패:", error);
                setLoading(false);
            });

        // 사용 가능한 쿠폰 가져오기
        axios.get(`http://localhost:8080/coupons/available/${user.userId}`)
            .then(response => {
                console.log("사용 가능한 쿠폰 목록:", response.data);
                // 저장
                setCoupons(response.data);
            })
            .catch(error => console.error("쿠폰 조회 실패:", error));
    }, [user]);

    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price.toLocaleString();  // 1,000 단위로 콤마 추가
    };


    // 선택한 쿠폰을 변경 // event.target.value 선택된 값 가져오기
    const handleCouponChange = (event) => {
        console.log("선택된 쿠폰 값:", event.target.value); // 디버깅용 로그 추가
        const selectedId = event.target.value ? Number(event.target.value) : null;

        console.log(`변환된 userCouponId: ${selectedId}`); // 변환 결과 확인
        setSelectedCouponId(selectedId);
    };




    // 쿠폰 적용 핸들러
    const handleApplyCoupon = async () => {
        if (!cart || !selectedCouponId) {
            alert("쿠폰을 선택해주세요.");
            return;
        }

        try {
            console.log(` 실제 적용될 userCouponId: ${selectedCouponId}`);  // 디버깅 로그 추가

            const response = await axios.post(
                `http://localhost:8080/cart/${cart.cartId}/apply-coupon/${selectedCouponId}`
            );

            console.log(" 쿠폰 적용 응답:", response.data);
            alert("쿠폰이 적용되었습니다!");

            // 최신 장바구니 데이터 다시 가져오기
            const updatedCart = await axios.get(`http://localhost:8080/cart/${user.userId}`);
            setCart(updatedCart.data);
        } catch (error) {
            console.error(" 쿠폰 적용 실패:", error.response?.data || error);
            alert(error.response?.data || "쿠폰 적용 실패");
        }
    };



    // 쿠폰 취소 버튼 추가
    const handleRemoveCoupon = async () => {
        if (!cart) return;

        try {
            await axios.delete(`http://localhost:8080/cart/${cart.cartId}/remove-coupon`);
            alert("쿠폰이 취소되었습니다!");
            setSelectedCouponId(null);

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


    // 결제 페이지로 이동하는 함수
    const handleCheckout = () => {
        if (!cart || cart.cartItems.length === 0) {
            alert("장바구니가 비어 있습니다.");
            return;
        }

        navigate(`/CartCheckout/${cart.cartId}`, { state: { cart } }); // cart 데이터 전달
    };

    if (loading) return <p>로딩 중...</p>;
    if (!cart || cart.cartItems.length === 0) return <p>장바구니가 비어 있습니다.</p>;

    return (
        <div className="cart-container">
            <div>
                <button onClick={() => navigate(-1)} className="back-button">
                    ← 뒤로가기
                </button>
            </div>
            <h2>장바구니</h2>
            {loading ? (
                <p>로딩 중...</p>
            ) : cart && cart.cartItems.length > 0 ? (
                <>
                    {/*상품 삭제 버튼 추가*/}
                    <div className="cart-list">
                        {cart.cartItems.map(item => (
                            <div key={item.cartItemId} className="cart-item">
                                <ul>
                                    <h3>{item.productName}</h3>
                                    <h3>가격: {formatPrice(item.productPrice)}원</h3>
                                    <h3>사이즈: {item.size || "사이즈 정보 없음"}</h3>
                                    <h3>수량: {item.qty}</h3>
                                </ul>
                                <button className="remove-button" onClick={() => handleRemoveItem(item.productId)}>삭제</button>
                            </div>
                        ))}
                    </div>


                    <div className="cart-summary">
                        <h3 className="cart-summary h3">총 가격: {formatPrice(cart.totalPrice)}원</h3>
                        {cart.discountAmount > 0 && <h3>할인 금액: -{formatPrice(cart.discountAmount)}원</h3>}
                        <h3 className="cart-summary h3">배송비 : {formatPrice(cart.shippingFee)}원</h3>
                        <h3 className="cart-summary h3">최종 결제 금액: {formatPrice(cart.finalPrice)}원</h3>
                    </div>
                </>
            ) : (
                <p className="empty-cart">장바구니가 비어 있습니다.</p>
            )}

            {/*결재확인*/}
            <div className="pay-checkout">
                <button className="checkout-button" onClick={handleCheckout}>
                    전체 구매
                </button>
            </div>

        </div>
    );
};

export default Cart;
