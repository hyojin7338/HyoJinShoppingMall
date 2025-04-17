import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import "../../styles/ChangeAddress.css"

const ChangeAddress = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { userId, productId, selectedSize, quantity, newAddressId } = location.state || {};

    const [addresses, setAddresses] = useState([]);
    const [selectedAddress, setSelectedAddress] = useState(null);

    useEffect(() => {
        if (!userId) return;

        axios.get(`http://15.164.216.15:8080/address/${userId}`)
            .then(response => {
                const fetchedAddresses = response.data;
                setAddresses(response.data);

                // 새로 추가된 주소 선택되도록 설정
                if (newAddressId) {
                    const newAddress = fetchedAddresses.find(addr => addr.addressId === newAddressId);
                    if (newAddress) setSelectedAddress(newAddress);
                }
            })
            .catch(error => {
                console.error("배송지 목록 불러오기 실패:", error);
            });
    }, [userId, newAddressId]);

    const handleSelectAddress = (address) => {
        setSelectedAddress(address);
    };

    const handleConfirm = () => {
        if (!selectedAddress) {
            alert("배송지를 선택해주세요.");
            return;
        }

        console.log("선택한 주소:", selectedAddress);
        console.log("전달할 newAddressId:", selectedAddress.addressId);

        navigate(`/Checkout/${productId}`, {
            state: {
                selectedAddress,
                selectedSize,
                quantity,
                productId,
                newAddressId: selectedAddress.addressId
            }
        });
    };


    const handleAddAddress = () => {
        navigate("/AddAddress", { state: { userId, productId } }); // 배송지 추가 페이지로 이동
    };

    return (
        <div className="change-address-container">
            <h2>배송지 변경 리스트</h2>
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>
            <ul className="address-list">
                {addresses.map(address => (
                    <li
                        key={address.addressId}
                        className={`address-item ${selectedAddress?.addressId === address.addressId ? "selected" : ""}`}
                        onClick={() => handleSelectAddress(address)}
                    >
                        <div className="address-select-indicator">
                            {selectedAddress?.addressId === address.addressId ? "✅" : "⬜"}
                        </div>
                        <div className="address-info">
                            <p><strong>{address.receiverName}</strong></p>
                            <p>{address.address}, {address.region}</p>
                            <p>연락처: {address.receiverTel}</p>
                        </div>
                    </li>
                ))}
            </ul>


            <button className="confirm-button" onClick={handleConfirm}>배송지 선택</button>
            <button className="add-address-button" onClick={handleAddAddress}>+ 새 배송지 추가</button>
        </div>
    );
};

export default ChangeAddress;
