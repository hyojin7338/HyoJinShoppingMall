import React, { useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext.jsx";
import axios from "axios";


const CartCheckout = () => {
    const { user } = useContext(UserContext);
    const location = useLocation();
    const { state } = location;
    const navigate = useNavigate();

    const selectedProducts = location.state?.selectedProducts || [];
    const { selectedAddress, productId, quantity } = state || {};

    const [checkoutData, setCheckoutData] = useState(null);
    const [availableCoupons, setAvailableCoupons] = useState([]);
    const [selectedCouponId, setSelectedCouponId] = useState("");
    const [discountAmount, setDiscountAmount] = useState(0);


    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        const addressId = state?.selectedAddress?.addressId || state?.newAddressId;

        if (addressId) {
            axios.get(`http://localhost:8080/address/detail/${addressId}`)
                .then(res => {
                    const addressData = res.data;
                    console.log("새로운 배송지 정보:", addressData);

                    setCheckoutData(prev => ({
                        ...prev,
                        name: addressData.receiverName,
                        tel: addressData.receiverTel,
                        address: addressData.address,
                        region: addressData.region,
                    }));
                })
                .catch(err => {
                    console.error("배송지 조회 실패:", err);
                });
        }

        fetchCartData();
    }, [user, productId, quantity, state?.newAddressId]);


    const fetchCartData = async () => {
        try {
            let response;

            if (selectedProducts.length > 0) {
                const selectedIds = selectedProducts.map(item => item.cartItemId);

                response = await axios.post(
                    `http://localhost:8080/cart/checkout/selected?userId=${user.userId}`,
                    selectedIds
                );
            }

            //console.log("✅ 결제 정보:", response.data);
            setCheckoutData(response.data);

            // 쿠폰 정보 가져오기 (유지)
            const couponResponse = await axios.get(`http://localhost:8080/coupons/available/${user.userId}`);
            console.log("✅ 사용 가능한 쿠폰:", couponResponse.data);
            setAvailableCoupons(couponResponse.data);
        } catch (error) {
            //console.error("❌ 데이터 불러오기 실패:", error);
        }
    };


    // 선택한 배송지가 있으면 checkoutData 업데이트
    useEffect(() => {
        // console.log("2. 주소 selectedAddress : ", selectedAddress)
        if (selectedAddress && checkoutData) {
            setCheckoutData(prevData => ({
                ...prevData,
                name: selectedAddress.receiverName,
                tel: selectedAddress.receiverTel,
                address: selectedAddress.address,
                region: selectedAddress.region
            }));
        }
    }, [selectedAddress]);

    // 쿠폰 적용 핸들러
    const handleSelectCoupon = (event) => {
        const couponId = event.target.value ? Number(event.target.value) : null;
        setSelectedCouponId(couponId);

        if (availableCoupons && couponId) {
            const coupon = availableCoupons.find(c => c.userCouponId === couponId);
            if (coupon && checkoutData?.totalPrice) {
                const discount = coupon.discountType === "PERCENT"
                    ? (checkoutData.totalPrice * coupon.discountValue) / 100
                    : coupon.discountValue;
                setDiscountAmount(discount);
            } else {
                setDiscountAmount(0);
            }
        }
    };


    // 쿠폰 적용
    const handleApplyCoupon = async () => {
        if (!checkoutData || !selectedCouponId || isNaN(Number(selectedCouponId))) {
            alert("쿠폰을 선택해주세요.");
            return;
        }

        try {
            const cartItemIds = selectedProducts?.map(item => item.cartItemId) || [];

            const updatedCheckoutData = await axios.post(
                `http://localhost:8080/order/cart/${user.userId}/selected`,
                 cartItemIds
            );

            console.log("✅ 쿠폰 적용 후 데이터:", updatedCheckoutData.data);
            setCheckoutData(updatedCheckoutData.data);
            setDiscountAmount(updatedCheckoutData.data.discountAmount || 0);
        } catch (error) {
            console.error("❌ 쿠폰 적용 실패:", error.response?.data || error);
            alert(error.response?.data || "쿠폰 적용 실패");
        }
    };

    // 쿠폰 취소
    const handleRemoveCoupon = async () => {
        if (!checkoutData) return;

        try {
            await axios.delete(`http://localhost:8080/cart/${checkoutData.cartId}/remove-coupon`);
            alert("쿠폰이 취소되었습니다!");

            // 최신 결제 정보 가져오기
            const updatedCheckoutData = await axios.post(
                `http://localhost:8080/order/cart/${user.userId}/selected`,
                selectedProducts.map(item => item.cartItemId)
            );

            console.log("✅ 쿠폰 적용 후 데이터:", updatedCheckoutData.data);

            setCheckoutData(updatedCheckoutData.data);
            setDiscountAmount(0); // 할인 금액 초기화
            setSelectedCouponId(null);
        } catch (error) {
            console.error("❌ 쿠폰 취소 실패:", error.response?.data || error);
            alert(error.response?.data || "쿠폰 취소 실패");
        }
    };


    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price ? price.toLocaleString() : "0";
    };


    const handlePayment = async () => {
        try {
            const { data } = await axios.post(
                `http://localhost:8080/order/cart/${user.userId}`,
                selectedProducts.map(item => item.cartItemId)
            );

            console.log("🛒 결제 완료:", data);

            alert("결제가 완료되었습니다!");
            // 2초 후 메인 페이지로 이동
            setTimeout(() => {
                navigate("/main");
            }, 2000);
        } catch (error) {
            alert(error.response?.data || "결제 실패");
        }
    };


    if (!checkoutData) return <p>로딩 중...</p>;

    return (
        <div className="checkout-container">
            <h2>결제 페이지</h2>
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>
            {/* 유저 정보 */}
            <div className="checkout-card">
                <h3>배송 정보</h3>
                <p><strong>이름:</strong> {state?.selectedAddress ? state.selectedAddress.receiverName : checkoutData?.name}</p>
                <p><strong>연락처:</strong> {state?.selectedAddress ? state.selectedAddress.receiverTel : checkoutData?.tel}</p>
                <p><strong>주소:</strong> {state?.selectedAddress ? `${state.selectedAddress.address}, ${state.selectedAddress.region}` : `${checkoutData?.address}, ${checkoutData?.region}`}</p>

                <button
                    className="checkout-button"
                    onClick={() => navigate('/ChangeAddress', { state: { userId: user.userId, productId } })}
                >
                    배송지 변경
                </button>

                <button
                    className="checkout-button reset-button"
                    onClick={() => {
                        setCheckoutData(null); // 기존 배송지 초기화
                        navigate(`/checkout/${productId}`, {
                            state: { product, selectedSize, quantity } }); // 현재 페이지 리로드
                    }}
                >
                    기본 배송지로 변경
                </button>
            </div>

            {/* 선택된 상품 목록 */}
            <div>
                {checkoutData.cartItems && checkoutData.cartItems.map((product, index) => (
                    <div key={product.cartItemId ? `cart-${product.cartItemId}` : `fallback-${index}`}>
                        <h3>{product.productName}</h3>
                        <p>가격: {formatPrice(product.itemPrice)}원</p>
                        <p>사이즈 : {product.size}</p>
                        <p>수량: {product.qty}</p>
                    </div>
                ))}
            </div>

            {/* 결제 금액 정보 */}
            <div className="payment-summary">
                <h3>총 결제 금액</h3>
                <p>상품 금액: {formatPrice(checkoutData.totalPrice)}원</p>
                <p>배송비: {formatPrice(checkoutData.shippingFee)}원</p>
                <p>할인 금액: -{formatPrice(discountAmount)}원</p>
                <hr />
                <h3>
                    최종 결제 금액:{" "}
                    {formatPrice(
                        checkoutData.totalPrice +
                        checkoutData.shippingFee -
                        discountAmount
                    )}원
                </h3>
            </div>

            {/* 쿠폰 선택 */}
            <div className="checkout-card">
                <h3>쿠폰 적용</h3>
                <select value={selectedCouponId || ""} onChange={handleSelectCoupon}>
                    <option value="">쿠폰 선택</option>
                    {availableCoupons
                        .filter(coupon => coupon.userCouponId !== null) // null 값 제거
                        .map((coupon, index) => (
                            <option key={`${coupon.userCouponId}_${index}`} value={coupon.userCouponId ?? ''}>
                                {coupon.name} ({coupon.discountValue}
                                {coupon.discountType === "PERCENT" ? "%" : "원"} 할인)
                            </option>
                        ))}
                </select>
            </div>

            <button onClick={handlePayment} className="payment-button">
                결제하기
            </button>
        </div>
    );
};

export default CartCheckout;
