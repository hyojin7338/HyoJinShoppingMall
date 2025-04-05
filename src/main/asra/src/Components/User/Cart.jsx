import React, { useEffect, useState, useContext, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { UserContext } from "../../context/UserContext.jsx";
import "../../styles/Cart.css";

const Cart = () => {
    const { user } = useContext(UserContext);
    const navigate = useNavigate();
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [checkedItems, setCheckedItems] = useState([]); // 선택한 상품 목록
    const [selectAll, setSelectAll] = useState(false); // 전체 선택 여부

    const selectedSummary = useMemo(() => {
        // cart가 null이면 빈 배열을 사용
        const selectedItems = (cart?.cartItems ?? []).filter(item => checkedItems.includes(item.cartItemId));
        const totalPrice = selectedItems.reduce((sum, item) => sum + item.productPrice * item.qty, 0);
        const shippingFee = totalPrice >= 100000 ? 0 : (totalPrice === 0 ? 0 : 3000); // 10만원 이상 무료, 0원이면 배송비도 0
        const finalPrice = totalPrice + shippingFee;

        return { totalPrice, shippingFee, finalPrice };
    }, [checkedItems, cart]); // cart가 변경될 때도 다시 계산하도록 수정

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
    }, [user]);

    // 선택된 상품 콘솔 출력
    useEffect(() => {
        if (checkedItems.length > 0) {
            const selectedProducts = cart.cartItems.filter(item => checkedItems.includes(item.cartItemId));
            console.log("선택된 상품 목록:", selectedProducts);
        }
    }, [checkedItems]);


    const formatPrice = (price) => price.toLocaleString();

    // 개별 체크박스 클릭 시 실행되는 함수
    const handleCheckboxChange = (cartItemId) => {
        setCheckedItems(prevChecked =>
            prevChecked.includes(cartItemId)
                ? prevChecked.filter(id => id !== cartItemId) // 체크 해제
                : [...prevChecked, cartItemId] // 체크 추가
        );
    };

    // 전체 선택 / 해제 기능
    const handleSelectAll = () => {
        if (selectAll) {
            setCheckedItems([]); // 전체 해제
        } else {
            setCheckedItems(cart.cartItems.map(item => item.cartItemId)); // 모든 상품 체크
        }
        setSelectAll(!selectAll);
    };


    const handleCheckout = () => {
        if (checkedItems.length === 0) {
            alert("구매할 상품을 선택하세요.");
            return;
        }

        // 선택된 상품 필터링
        const selectedProducts = cart.cartItems.filter(item => checkedItems.includes(item.cartItemId));

        // 결제 페이지로 이동하며 선택된 상품 데이터 전달
        navigate(`/CartCheckout`, {
            state: { selectedProducts
            }
        });
    };


    if (loading) return <p>로딩 중...</p>;
    if (!cart || cart.cartItems.length === 0) return <p>장바구니가 비어 있습니다.</p>;

    return (
        <div className="cart-container">
            <h2>장바구니</h2>
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>

            {/* 전체 선택 체크박스 */}
            <div className="select-all-container">
                <input
                    type="checkbox"
                    checked={selectAll}
                    onChange={handleSelectAll}
                    id="selectAll"
                />
                <label htmlFor="selectAll">전체 선택</label>
            </div>

            <div className="cart-list">
                {cart.cartItems.map(item => (
                    <div key={item.cartItemId} className={`cart-item ${checkedItems.includes(item.cartItemId) ? 'checked' : ''}`}>
                        <input
                            type="checkbox"
                            checked={checkedItems.includes(item.cartItemId)}
                            onChange={() => handleCheckboxChange(item.cartItemId)}
                        />
                        <ul>
                            <h3>{item.productName}</h3>
                            <h3>가격: {formatPrice(item.productPrice)}원</h3>
                            <h3>사이즈: {item.size || "사이즈 정보 없음"}</h3>
                            <h3>수량: {item.qty}</h3>
                        </ul>
                        <button className="remove-button">삭제</button>
                    </div>
                ))}
            </div>

            <div className="cart-summary">
                <h3>총 가격: {formatPrice(selectedSummary.totalPrice)}원</h3>
                <h3>배송비: {formatPrice(selectedSummary.shippingFee)}원</h3>
                <h3>최종 결제 금액: {formatPrice(selectedSummary.finalPrice)}원</h3>
            </div>

            <div className="pay-checkout">
                <button className="checkout-button" onClick={handleCheckout}>구매하기</button>
            </div>
        </div>
    );
};

export default Cart;
