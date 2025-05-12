import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { MasterContext } from "../../context/MasterContext.jsx";
import { useNavigate } from "react-router-dom";

const MasterQuestion = () => {
    const {master} = useContext(MasterContext);
    const navigate = useNavigate();
    const [questions, setQuestions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!master) {
            setError("판매자 정보를 불러올 수 없습니다.");
            setLoading(false);
            return;
        }


        const fetchQuestions = async () => {
            try {
                const response = await axios.get(`http://15.164.216.15/api/product/product_MasterQuestion/${master.masterId}`,{
                    withCredentials: true,
                });
                setQuestions(Array.isArray(response.data) ? response.data : []); // 응답이 배열인지 확인 후 저장
            } catch (err) {
                console.error("API 요청 중 오류 발생:", err);
                setError("문의 목록을 불러오는 중 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchQuestions();
        console.log("로그인 상태:", master);
    }, [master]);

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h2>고객 문의 목록</h2>
            <button onClick={() => navigate(-1)}>뒤로가기</button>
            {Array.isArray(questions) && questions.length === 0 ? (
                <p>문의가 없습니다.</p>
            ) : Array.isArray(questions) ? (
                <table>
                    <thead>
                    <tr>
                        <th>번호</th>
                        <th>작성자</th>
                        <th>상품명</th>
                        <th>문의 제목</th>
                        <th>문의 유형</th>
                        <th>답변 여부</th>
                        <th>상세 보기</th>
                    </tr>
                    </thead>
                    <tbody>
                    {questions.map((q, index) => (
                        <tr key={q.productQuestionId}>
                            <td>{index + 1}</td>
                            <td>{q.userName}</td>
                            <td>{q.productName}</td>
                            <td>{q.title}</td>
                            <td>{q.questionType}</td>
                            <td>{q.isAnswered ? "✅" : "❌"}</td>
                            <td>
                                <button onClick={() => navigate(`/master/question/${q.productQuestionId}`)}>
                                    상세 보기
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p>데이터가 올바르지 않습니다.</p>
            )}
        </div>
    );
}
export default MasterQuestion;
