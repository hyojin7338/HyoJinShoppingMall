import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import "../styles/MyInquiry.css"; // 스타일 추가

const MyInquiry = () => {
    const { user } = useContext(UserContext);
    const userId = user?.userId;

    const [activeTab, setActiveTab] = useState("product"); // 탭 상태
    const [productInquiries, setProductInquiries] = useState([]);
    const [adminInquiries, setAdminInquiries] = useState([]);
    const [error, setError] = useState(null);


    useEffect(() => {
        if (!userId) {
            setError("로그인이 필요합니다.");
            return;
        }

        console.log("운영자에게 문의 유저 정보 :", user);

        axios
            .get(`http://localhost:8080/product/productQuestion/${userId}`)
            .then((res) => setProductInquiries(res.data))
            .catch((err) => {
                console.error("상품 문의 오류:", err);
                setError("상품 문의를 불러오는 데 실패했습니다.");
            });

        // 운영자 문의 조회
        axios
            .get(`http://localhost:8080/general/userQuestion/${userId}`)
            .then((res) => setAdminInquiries(res.data))
            .catch((err) => {
                console.error("운영자 문의 오류:", err);
                setError("운영자 문의를 불러오는 데 실패했습니다.");
            });

    }, [userId, user]);



    return (
        <div className="my-inquiries">
            <div className="tabs">
                <button
                    className={activeTab === "product" ? "active" : ""}
                    onClick={() => setActiveTab("product")}
                >
                    상품 문의
                </button>
                <button
                    className={activeTab === "admin" ? "active" : ""}
                    onClick={() => setActiveTab("admin")}
                >
                    운영자 문의
                </button>
            </div>

            {error && <p className="error-message">{error}</p>}

            {!user ? (
                <p>로그인 후 이용해주세요.</p>
            ) : activeTab === "product" ? (
                <>
                    <h2>내가 작성한 상품 문의</h2>
                    {productInquiries.length === 0 ? (
                        <p>작성한 상품 문의가 없습니다.</p>
                    ) : (
                        <ul className="inquiry-list">
                            {productInquiries.map((inquiry, index) => (
                                <li key={inquiry.id || index} className="inquiry-item">
                                    <h3 className="inquiry-title">{inquiry.title}</h3>
                                    <p className="inquiry-content">{inquiry.contents}</p>
                                    <p className="inquiry-userName">작성자: {inquiry.userName}</p>
                                    <p className="inquiry-productName">상품명: {inquiry.productName}</p>
                                    <p className="inquiry-questionType">문의유형: {inquiry.questionType}</p>
                                </li>
                            ))}
                        </ul>
                    )}
                </>
            ) : (
                <>
                    <h2>내가 작성한 운영자 문의</h2>
                    {adminInquiries.length === 0 ? (
                        <p>작성한 운영자 문의가 없습니다.</p>
                    ) : (
                        <ul className="inquiry-list">
                            {adminInquiries.map((inquiry, index) => (
                                <li key={inquiry.id || index} className="inquiry-item">
                                    <h3 className="inquiry-title">{inquiry.title}</h3>
                                    <p className="inquiry-content">{inquiry.contents}</p>
                                    <p className="inquiry-questionType">문의유형: {inquiry.questionType}</p>
                                </li>
                            ))}
                        </ul>
                    )}
                </>
            )}
        </div>
    );
};
export default MyInquiry;
