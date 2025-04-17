import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { UserContext } from "../../context/UserContext.jsx";
import "../../styles/AdminByQuestion.css"; // css 파일 import

// 일반 유저 문의 유형 enum 리스트
const userQuestionTypes = [
    { value: "ACCOUNT_ISSUE", label: "계정 관련 문제" },
    { value: "ORDER_ISSUE", label: "주문 관련 문의" },
    { value: "PAYMENT_PROBLEM", label: "결제 문제" },
    { value: "COUPON_ISSUE", label: "쿠폰 및 프로모션 관련 문의" },
    { value: "PRODUCT_INQUIRY", label: "특정 상품 관련 문의" },
    { value: "GENERAL_FEEDBACK", label: "일반적인 피드백 및 건의사항" },
    { value: "REPORT_ISSUE", label: "신고 관련 문의" },
    { value: "OTHER", label: "기타 문의" }
];

const AdminByQuestion = () => {
    const { user } = useContext(UserContext);
    const [questionType, setQuestionType] = useState("");
    const [title, setTitle] = useState("");
    const [contents, setContents] = useState("");

    useEffect(() => {
        console.log("운영자에게 문의 :", user);
    }, [user]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!questionType || !title || !contents) {
            alert("모든 항목을 입력해주세요.");
            return;
        }

        const requestData = {
            questionType,
            userId: user.userId,
            title,
            contents,
            role:user.role
        };

        try {
            await axios.post("http://15.164.216.15:8080/general/question", requestData);
            alert("문의가 등록되었습니다.");
            setQuestionType("");
            setTitle("");
            setContents("");
        } catch (err) {
            console.error("문의 등록 실패:", err);
            alert("문의 등록 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="admin-question-container">
            <h2>운영자 문의</h2>
            <form onSubmit={handleSubmit} className="admin-question-form">
                <div className="admin-question-field">
                    <label>문의 유형</label>
                    <select
                        value={questionType}
                        onChange={(e) => setQuestionType(e.target.value)}
                        required
                    >
                        <option value="">-- 문의 유형 선택 --</option>
                        {userQuestionTypes.map((type) => (
                            <option key={type.value} value={type.value}>
                                {type.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="admin-question-field">
                    <label>제목</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>

                <div className="admin-question-field">
                    <label>문의 내용</label>
                    <textarea
                        value={contents}
                        onChange={(e) => setContents(e.target.value)}
                        required
                    />
                </div>

                <button type="submit">문의 등록</button>
            </form>
        </div>
    );
};

export default AdminByQuestion;
