import React, { useEffect, useState, useContext } from "react";
import { UserContext } from "../../context/UserContext.jsx";
import axios from "axios";
import "../../styles/Order.css";

const Order = () => {
    const { user } = useContext(UserContext);
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        if (user) {
            axios.get(`http://15.164.216.15/api/order/getOrderByUser/${user.userId}`,{
                withCredentials: true,
            })
                .then(response => {
                    setOrders(response.data);
                })
                .catch(error => {
                    console.error("주문 내역 조회 실패:", error);
                });
        }
    }, [user]);

    if (!user) return <p>로그인이 필요합니다.</p>;

    return (
        <div className="order-container">
            <h2>🛒 나의 주문 내역</h2>
            {orders.length === 0 ? (
                <p>주문 내역이 없습니다.</p>
            ) : (
                orders.map(order => (
                    <div key={order.orderId} className="order-box">
                        <h3>주문번호: {order.orderId}</h3>
                        <p>총 가격: {order.totalPrice.toLocaleString()}원</p>
                        <p>배송비: {order.shippingFee.toLocaleString()}원</p>
                        <p>주문 상태: {order.orderStatus}</p>
                        <p>주문 날짜: {new Date(order.orderDate).toLocaleString()}</p>


                    </div>
                ))
            )}
        </div>
    );
};

export default Order;
