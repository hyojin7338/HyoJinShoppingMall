import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import "../../styles/AddAddress.css";

const AddAddress = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { userId, productId,selectedSize, quantity } = location.state || {};


    const [formData, setFormData] = useState({
        receiverName: "",
        receiverTel: "",
        address: "",
        region: ""
    });

    useEffect(() => {
        // 카카오 주소찾기 스크립트 로드
        const script = document.createElement("script");
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        script.async = true;
        document.body.appendChild(script);

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const findAddress = () => {
        new window.daum.Postcode({
            oncomplete: (data) => {
                let addr = data.userSelectedType === "R" ? data.roadAddress : data.jibunAddress;
                let extraAddr = "";

                if (data.userSelectedType === "R") {
                    if (data.bname && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName && data.apartment === "Y") {
                        extraAddr += (extraAddr ? ", " + data.buildingName : data.buildingName);
                    }
                    if (extraAddr) {
                        addr += ` (${extraAddr})`;
                    }
                }

                setFormData((prevData) => ({
                    ...prevData,
                    address: addr
                }));
            }
        }).open();
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!userId) {
            alert("사용자 정보가 없습니다.");
            return;
        }

        axios.post(`http://15.164.216.15/api/address/add/${userId}`, formData,{
            withCredentials: true,
        })
            .then(response => {
                const newAddressId = response.data.addressId;
                console.log("새로운 배송지 정보:", newAddressId);
                alert("배송지가 성공적으로 추가되었습니다.");
                navigate("/ChangeAddress", {
                    userId,
                    productId,
                    selectedSize,
                    quantity,
                    newAddressId
                }); // 이전 페이지(배송지 선택 페이지)로 돌아가기
            })
            .catch(error => {
                console.error("배송지 추가 실패:", error);
                alert("배송지 추가에 실패했습니다.");
            });

    };

    return (
        <div className="add-address-container">
            <h2>배송지 추가</h2>
            <div>
                <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            </div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>수령인</label>
                    <input
                        type="text"
                        name="receiverName"
                        value={formData.receiverName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>연락처</label>
                    <input
                        type="text"
                        name="receiverTel"
                        value={formData.receiverTel}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>주소</label>
                    <input
                        type="text"
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        readOnly
                        required
                    />
                    <button type="button" onClick={findAddress}>
                        우편번호 찾기
                    </button>
                </div>
                <div>
                    <label>상세주소</label>
                    <input
                        type="text"
                        name="region"
                        value={formData.region}
                        onChange={handleChange}
                        placeholder="상세 주소를 입력하세요"
                        required
                    />
                </div>

                <button type="submit">저장하기</button>
            </form>
        </div>
    );
};

export default AddAddress;