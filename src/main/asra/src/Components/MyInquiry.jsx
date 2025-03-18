import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import "../styles/MyInquiry.css"; // 스타일 추가

const MyInquiry = () => {
    const [inquiries, setInquiries] = useState([]); // 문의 목록 상태
    const [error, setError] = useState(null);
    const { user } = useContext(UserContext);
    const userId = user?.userId; // userId 가져오기 (수정)

    useEffect(() => {
        if (!userId) {
            setError("로그인이 필요합니다.");
            return;
        }

        axios
            .get(`http://localhost:8080/product/productQuestion/${userId}`)
            .then((response) => {
                setInquiries(response.data);
            })
            .catch((err) => {
                console.error("문의 목록을 불러오는 중 오류 발생:", err);
                setError("문의 목록을 불러오는 데 실패했습니다.");
            });
    }, [userId]);

    return (
        <div className="my-inquiries">
            <h2>내가 작성한 문의</h2>

            {error && <p className="error-message">{error}</p>}

            {!user ? (
                <p>로그인 후 이용해주세요.</p>
            ) : inquiries.length === 0 ? (
                <p>작성한 문의가 없습니다.</p>
            ) : (
                <ul className="inquiry-list">
                    {inquiries.map((inquiry, index) => (
                        <li key={inquiry.id || index} className="inquiry-item">
                            <h3 className="inquiry-title">{inquiry.title}</h3>
                            <p className="inquiry-content">{inquiry.contents}</p>
                            <p className="inquiry-userName">작성자 : {inquiry.userName}</p>
                            <p className="inquiry-productName">상품명 : {inquiry.productName}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default MyInquiry;
