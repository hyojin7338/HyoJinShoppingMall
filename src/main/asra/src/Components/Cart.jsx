import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import "../styles/Cart.css";

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

    if (loading) return <p>로딩 중...</p>;
    if (!cart || cart.cartItems.length === 0) return <p>장바구니가 비어 있습니다.</p>;

    return (
        <div className="cart-container">
            <h2>장바구니</h2>
            <div className="cart-list">
                {cart.cartItems.map(item => (
                    <div key={item.cartItemId} className="cart-item">
                        <h3>{item.productName}</h3>
                        <p>가격: {item.productPrice}원</p>
                        <p>수량: {item.qty}</p>
                    </div>
                ))}
            </div>
            <h3>총 가격: {cart.totalPrice}원</h3>
            <h3>최종 결제 금액: {cart.finalPrice}원</h3>
        </div>
    );
};

export default Cart;
