import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

const ProductQuestion = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { productId, userId } = location.state || {};

    const [questionType, setQuestionType] = useState("PRODUCT_INFO");
    const [title, setTitle] = useState("");
    const [contents, setContents] = useState("");

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!title || !contents) {
            alert("제목과 내용을 입력해주세요.");
            return;
        }

        try {
            await axios.post("http://15.164.216.15/api/product/questionCreate", {
                productId,
                userId,
                questionType,
                title,
                contents
            }
            ,{
                    withCredentials: true,
                });

            alert("문의가 등록되었습니다.");
            navigate(`/product/${productId}`);
        } catch (error) {
            console.error("문의 등록 실패:", error);
            alert("문의 등록에 실패했습니다.");
        }
    };

    return (
        <div>
            <h2>상품 문의하기</h2>
            <form onSubmit={handleSubmit}>
                <label>문의 유형</label>
                <select value={questionType} onChange={(e) => setQuestionType(e.target.value)}>
                    <option value="PRODUCT_INFO">상품 정보 문의</option>
                    <option value="STOCK">재고 관련 문의</option>
                    <option value="DELIVERY">배송 관련 문의</option>
                    <option value="RETURN">반품/교환 문의</option>
                    <option value="ETC">기타 문의</option>
                </select>

                <label>제목</label>
                <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />

                <label>내용</label>
                <textarea value={contents} onChange={(e) => setContents(e.target.value)} />

                <button type="submit">문의 등록</button>
            </form>
        </div>
    );
};


export default ProductQuestion;