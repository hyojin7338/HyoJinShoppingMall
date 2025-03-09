import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import "../styles/ChangeAddress.css"

const ChangeAddress = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { userId } = location.state || {};

    const [addresses, setAddresses] = useState([]);
    const [selectedAddress, setSelectedAddress] = useState(null);

    useEffect(() => {
        if (!userId) return;

        axios.get(`http://localhost:8080/address/${userId}`)
            .then(response => {
                setAddresses(response.data);
            })
            .catch(error => {
                console.error("배송지 목록 불러오기 실패:", error);
            });
    }, [userId]);

    const handleSelectAddress = (address) => {
        setSelectedAddress(address);
    };

    const handleConfirm = () => {
        if (!selectedAddress) {
            alert("배송지를 선택해주세요.");
            return;
        }

        navigate("/Checkout", { state: { selectedAddress } }); // 선택한 주소를 Checkout으로 전달
    };


    const handleAddAddress = () => {
        navigate("/AddAddress", { state: { userId } }); // 배송지 추가 페이지로 이동
    };

    return (
        <div className="change-address-container">
            <h2>배송지 변경 리스트</h2>
            <ul className="address-list">
                {addresses.map(address => (
                    <li
                        key={address.addressId}
                        className={`address-item ${selectedAddress?.addressId === address.addressId ? "selected" : ""}`}
                        onClick={() => handleSelectAddress(address)}
                    >
                        <p><strong>{address.receiverName}</strong></p>
                        <p>{address.address}, {address.region}</p>
                        <p>연락처: {address.receiverTel}</p>
                    </li>
                ))}
            </ul>

            <button className="confirm-button" onClick={handleConfirm}>배송지 선택</button>
            <button className="add-address-button" onClick={handleAddAddress}>+ 새 배송지 추가</button>
        </div>
    );
};

export default ChangeAddress;
