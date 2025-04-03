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

    const { selectedAddress } = location.state || {};

    const [checkoutData, setCheckoutData] = useState(null);
    const [availableCoupons, setAvailableCoupons] = useState([]);
    const [selectedCouponId, setSelectedCouponId] = useState(null);
    const [discountAmount, setDiscountAmount] = useState(0);


    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        const addressId = state?.selectedAddress?.addressId || state?.newAddressId;

        // console.log("1. AddressID 유무 : ", addressId)

        // 배송지 선택한 경우, 해당 배송지 정보 별도 조회
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

        const fetchCartData = async () => {
            try {
                const cartResponse = await axios.get(`http://localhost:8080/cart/checkout/${user.userId}`);
                console.log("장바구니 결제 정보:", cartResponse.data);
                setCheckoutData(cartResponse.data);

                const couponResponse = await axios.get(`http://localhost:8080/coupons/${user.userId}`);
                console.log("사용 가능한 쿠폰:", couponResponse.data);
                setAvailableCoupons(couponResponse.data);
            } catch (error) {
                console.error("데이터 불러오기 실패:", error);
            }
        };

        fetchCartData();
    }, [user, state?.newAddressId]);


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
        setSelectedCoupon(couponId);

        // 선택한 쿠폰이 있다면 할인 금액 계산
        if (checkoutData && checkoutData.availableCoupons) {
            const coupon = checkoutData.availableCoupons.find(c => c.userCouponId === couponId);
            if (coupon) {
                setDiscountAmount(
                    coupon.discountType === "PERCENT"
                        ? (checkoutData.amount * coupon.discountValue) / 100
                        : coupon.discountValue
                );
            } else {
                setDiscountAmount(0);
            }
        }
    };

    // 쿠폰 적용
    const handleApplyCoupon = async () => {
        if (!checkoutData || !selectedCouponId || Number.isNaN(selectedCouponId)) {
            alert("쿠폰을 선택해주세요.");
            return;
        }

        try {
            const response = await axios.post(
                `http://localhost:8080/cart/${checkoutData.cartId}/apply-coupon/${selectedCouponId}`
            );
            console.log("✅ 쿠폰 적용 성공:", response.data);

            // 최신 결제 정보 가져오기
            const updatedCheckoutData = await axios.post(
                `http://localhost:8080/order/cart/${user.userId}/selected`,
                selectedProducts.map(item => item.cartItemId)
            );

            console.log("✅ 최신 결제 정보:", updatedCheckoutData.data);

            setCheckoutData(updatedCheckoutData.data);
            setDiscountAmount(updatedCheckoutData.data.discountAmount || 0);  // 할인 금액 반영
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
            console.error("❌ 결제 실패:", error.response?.data || error);
            alert(error.response?.data || "결제 실패");
        }
    };

    console.log("최종 CheckoutData:", checkoutData, "결제 페이지 유저 ID " ,user.userId);

    if (!checkoutData) return <p>로딩 중...</p>;

    return (
        <div className="checkout-container">
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>
            <h2>결제 페이지</h2>

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
                        navigate(`/checkout/${productId}`, {  state: { product, selectedSize, quantity } }); // 현재 페이지 리로드
                    }}
                >
                    기본 배송지로 변경
                </button>
            </div>

            {/* 선택된 상품 목록 */}
            <div>
                {checkoutData.cartItems.map((product, index) => (
                    <div key={product.cartItemId ?? `fallback-${index}`}>
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
                <h3>최종 결제 금액: {formatPrice(checkoutData.finalPrice)}원</h3>
            </div>

            {/* 쿠폰 선택 */}
            <div className="coupon-selection">
                <h3>쿠폰 선택</h3>
                {availableCoupons.length > 0 ? (
                    <select onChange={handleSelectCoupon} value={selectedCouponId ?? ""}>
                        <option value="">쿠폰을 선택하세요</option>
                        {availableCoupons.map((coupon, index) => (
                            <option key={coupon.userCouponId ?? `coupon-${index}`} value={coupon.userCouponId}>
                                {coupon.name} - 할인: {formatPrice(coupon.discountValue)}
                            </option>
                        ))}
                    </select>
                ) : (
                    <p>사용 가능한 쿠폰이 없습니다.</p>
                )}
                <button onClick={handleApplyCoupon} disabled={!selectedCouponId}>
                    쿠폰 적용
                </button>
                <button onClick={handleRemoveCoupon} disabled={!selectedCouponId}>
                    쿠폰 취소
                </button>
            </div>


            <button onClick={handlePayment} className="payment-button">
                결제하기
            </button>

        </div>
    );
};

export default CartCheckout;
