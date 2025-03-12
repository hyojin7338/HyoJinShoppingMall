import React, { useContext, useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import axios from "axios";
import "../styles/Checkout.css";

const Checkout = () => {
    const { user } = useContext(UserContext);
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();
    const { state } = location;
    const { selectedAddress } = location.state || {};

    const [selectedCoupon, setSelectedCoupon] = useState(null);
    const [discountAmount, setDiscountAmount] = useState(0);
    const [checkoutData, setCheckoutData] = useState(null);

    // 선택한 사이즈와 수량을 가져옴
    const selectedSize = state?.selectedSize || "선택 안됨";
    const quantity = state?.quantity || 1;



    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }
        const addressId = state?.selectedAddress?.addressId || state?.newAddressId;

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

        //  백엔드에서 구매 전 정보 조회
        axios.get(`http://localhost:8080/product/${user.userId}/${productId}/${quantity}`)
            .then(response => {
                console.log(" 구매 전 정보:", response.data);
                setCheckoutData(response.data);
            })
            .catch(error => {
                console.error(" 구매 전 정보 조회 실패:", error);
            });
    }, [user, productId,  state?.newAddressId]);

    // 선택한 배송지가 있으면 checkoutData 업데이트
    useEffect(() => {
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
    const finalPrice = checkoutData ? (checkoutData.amount * quantity) - discountAmount : 0;

    // 결제 처리
    const handlePayment = () => {
        alert("결제가 완료되었습니다!");
        navigate("/"); // 결제 완료 후 홈으로 이동
    };

    const formatPrice = (price) => {
        return typeof price === "number" ? price.toLocaleString() : "0";
    };

    if (!checkoutData) return <p>로딩 중...</p>;

    return (
        <div className="checkout-container">
            <h2>주문 확인</h2>

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

            {/* 상품 정보 */}
            <div className="checkout-card">
                <h3>상품 정보</h3>
                <div className="product-summary">
                    <img src={checkoutData.mainImgUrl} alt={checkoutData.productName} className="product-image" />
                    <div>
                        <p><strong>{checkoutData.productName}</strong></p>
                        <p>상품 설명: {checkoutData.contents}</p>
                        <p>가격: {formatPrice(checkoutData.amount)}원</p>
                        <p>판매자: {checkoutData.businessName}</p>
                        <p>재고 수량: {checkoutData.cnt}개</p>
                        <p><strong>선택한 사이즈:</strong> {selectedSize}</p>
                        <p><strong>선택한 수량:</strong> {quantity}개</p>
                    </div>
                </div>
            </div>

            {/* 쿠폰 적용 */}
            <div className="checkout-card">
                <h3>쿠폰 적용</h3>
                <select className="coupon-select" value={selectedCoupon || ""} onChange={handleCouponChange}>
                    <option value="">쿠폰을 선택하세요</option>
                    {checkoutData.availableCoupons?.map(coupon => (
                        <option key={coupon.userCouponId} value={coupon.userCouponId}>
                            {coupon.name} ({coupon.discountValue}{coupon.discountType === "PERCENT" ? "%" : "원"} 할인)
                        </option>
                    ))}
                </select>
            </div>

            {/* 결제 금액 */}
            <div className="checkout-summary">
                <h3>결제 금액</h3>
                <p>상품 금액: {formatPrice(checkoutData.totalPrice)}원</p>
                <p>배송비: {formatPrice(checkoutData.shippingFee)}원</p>
                <p>할인 금액: -{formatPrice(discountAmount)}원</p>
                <hr />
                <h3>최종 결제 금액: {formatPrice(finalPrice)}원</h3>
            </div>

            <button className="checkout-button" onClick={handlePayment}>결제하기</button>
        </div>
    );
};

export default Checkout;
