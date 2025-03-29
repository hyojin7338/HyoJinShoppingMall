import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { MasterContext } from "../../context/MasterContext.jsx";
import "../../styles/MasterQuestionDetail.css"

const MasterQuestionDetail = () => {
    const {master} = useContext(MasterContext);

    const { productQuestionId } = useParams();
    const navigate = useNavigate();
    const [question, setQuestion] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    const [showAnswerForm, setShowAnswerForm] = useState(false); // 답변 입력 폼 보이기 여부
    const [answerText, setAnswerText] = useState(""); // 입력한 답변 내용
    const [answers, setAnswers] = useState([]);  // 답변 목록 상태 추가


    useEffect(() => {
        console.log("로그인 상태:", master);
    }, [master]);

    useEffect(() => {
        const fetchQuestionDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/master/question/${productQuestionId}`);
                setQuestion(response.data);
            } catch (err) {
                console.error("문의 상세 조회 중 오류 발생:", err);
                setError("문의 내용을 불러오는 중 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        const fetchAnswers = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/product/answerCreate/${productQuestionId}`);
                setAnswers(response.data);
            } catch (err) {
                console.error("답변 조회 오류:", err);
            }
        };

        fetchQuestionDetail();
        fetchAnswers();
    }, [productQuestionId]);


    const handleAnswerSubmit = async () => {
        if (!answerText.trim()) {
            alert("답변 내용을 입력해주세요.");
            return;
        }


        try {
            await axios.post("http://localhost:8080/product/answerCreate", {
                productQuestionId,
                masterId: master.masterId,
                answerContents: answerText
            });

            setAnswerText(""); // 입력 필드 초기화
            setShowAnswerForm(false); // 입력 폼 닫기

            // 답변 목록 다시 불러오기
            const response = await axios.get(`http://localhost:8080/product/answerCreate/${productQuestionId}`);
            setAnswers(response.data);

        } catch (err) {
            console.error("답변 저장 오류:", err);
            alert("답변 저장 중 오류가 발생했습니다.");
        }
    };

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;
    if (!question) return <p>문의 정보를 찾을 수 없습니다.</p>;

    return (
        <div>
            <h2>문의 상세 내용</h2>
            <button onClick={() => navigate(-1)}>뒤로가기</button> {/* 뒤로가기 버튼 */}
            <table>
                <tbody>
                <tr>
                    <th>작성자</th>
                    <td>{question.userName}</td>
                </tr>
                <tr>
                    <th>상품명</th>
                    <td>{question.productName}</td>
                </tr>
                <tr>
                    <th>문의 제목</th>
                    <td>{question.title}</td>
                </tr>
                <tr>
                    <th>문의 내용</th>
                    <td>{question.contents}</td>
                </tr>
                <tr>
                    <th>문의 유형</th>
                    <td>{question.questionType}</td>
                </tr>
                <tr>
                    <th>답변 여부</th>
                    <td>{question.isAnswered ? "✅ 답변 완료" : "❌ 미답변"}</td>
                </tr>
                </tbody>
            </table>
            {/* 답변 리스트 */}
            <h3>답변</h3>
            {answers.length === 0 ? <p>아직 답변이 없습니다.</p> : (
                <ul>
                    {answers.map((answer) => (
                        <li key={answer.productAnswerId}>
                            <p><strong>{answer.masterName}</strong> ({new Date(answer.answeredAt).toLocaleString()})</p>
                            <p>{answer.answerContents}</p>
                        </li>
                    ))}
                </ul>
            )}

            <div>
                <h3>답변 목록</h3>
                {answers.length > 0 ? (
                    <ul>
                        {answers.map((answer) => (
                            <li key={answer.productAnswerId}>
                                <p><strong>답변:</strong> {answer.answerContents}</p>
                                <p><strong>마스터:</strong> {answer.masterName}</p>
                                <p><strong>작성 시간:</strong> {new Date(answer.createdAt).toLocaleString()}</p>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>아직 답변이 없습니다.</p>
                )}
            </div>


            {/* 답변 입력 폼 */}
            {!showAnswerForm && (
                <button onClick={() => setShowAnswerForm(true)}>답글 달기</button>
            )}

            {showAnswerForm && (
                <div>
                    <textarea
                        value={answerText}
                        onChange={(e) => setAnswerText(e.target.value)}
                        placeholder="답변을 입력하세요..."
                        rows="4"
                        cols="50"
                    />
                    <br />
                    <button onClick={handleAnswerSubmit}>답변 제출</button>
                    <button onClick={() => setShowAnswerForm(false)}>취소</button>
                </div>
            )}
        </div>

    );
};

export default MasterQuestionDetail;
