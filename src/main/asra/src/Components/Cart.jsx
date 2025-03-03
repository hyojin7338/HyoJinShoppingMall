import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import "../styles/Cart.css";
import FooterNav from "../Components/FooterNav.jsx";

const Cart = () => {
    const { user } = useContext(UserContext);  // 로그인한 유저 정보 가져오기
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);

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

    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price.toLocaleString();  // 1,000 단위로 콤마 추가
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
            console.error("상품 삭제 실패:", error);
            alert("상품 삭제에 실패했습니다.");
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
                                <button className="remove-button" onClick={() => handleRemoveItem(item.productId)}>
                                    삭제
                                </button>
                            </div>
                        ))}
                    </div>
                    <h3>총 가격: {formatPrice(cart.totalPrice)}원</h3>
                    <h3>배송비 : {formatPrice(cart.shippingFee)}원</h3>
                    <h3>최종 결제 금액: {formatPrice(cart.finalPrice)}원</h3>
                </>
            ) : (
                <p className="empty-cart">장바구니가 비어 있습니다.</p>
            )}

            {/*  장바구니가 비어 있어도 항상 FooterNav를 보여줌 */}
            <FooterNav />
        </div>
    );
};

export default Cart;
