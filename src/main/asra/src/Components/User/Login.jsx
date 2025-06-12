import React, {useEffect, useState, useContext} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {UserContext} from "../../context/UserContext.jsx";
import "../../styles/Login.css"

const Login = () => {
    const [formData, setFormData] = useState({code: "", password: "",});
    const [rememberCode, setRememberCode] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();
    const {setUser} = useContext(UserContext);

    // 로컬 스토리지에서 저장 된 아이디(Code) 불러오기
    useEffect(() => {
        const savedCode = localStorage.getItem("savedCode"); // 저장된 아이디 가져오기
        const savedRemember = localStorage.getItem("rememberCode") === "true"; // 저장된 체크 여부 확인

        if (savedCode) {
            setFormData((prev) => ({...prev, code: savedCode}));
            setRememberCode(savedRemember); //  로그인 후에도 체크박스 상태 유지
        }
    }, []);

    // 폼 입력 핸들러
    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData((prev) => ({...prev, [name]: value}));
    };

    // 로그인 요청 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault(); // submit 동안 다른 이벤트가 발생하지 않도록
        setError("");
        setSuccess("");

        if (!formData.code || !formData.password) {
            setError("아이디와 비밀번호를 입력해주세요. ");
            return;
        }

        try {
            const response = await axios.post("http://15.164.216.15/api/login", formData,{
                withCredentials: true,
            });
            console.log("로그인 API 응답 데이터:", response.data);

            const {accessToken, refreshToken, userId, name, cartId, role} = response.data;

            if (!userId || !name || !cartId) {
                console.error("로그인 응답에 user 정보가 없습니다!");
                return;
            }

            const userData = { userId, name, cartId, role };


            // 이메일 기억하기
            if (rememberCode) {
                localStorage.setItem("savedCode", formData.code);
                localStorage.setItem("rememberCode", "true"); // 체크 여부 저장
            } else {
                localStorage.removeItem("savedCode");
                localStorage.setItem("rememberCode", "false"); // 체크 해제 상태 저장
            }

            // JWT 토큰 저장
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            localStorage.setItem("user", JSON.stringify(userData));

            //  로그인한 유저 정보를 UserContext에 업데이트
            setUser(userData);
            console.log("로그인한 유저 정보:", userData);

            alert(`환영합니다! ${name}님`);
            navigate("/main"); //  메인 화면으로 이동

        } catch (err) {
            if (err.response) {
                alert(err.response.data.message || "아이디 또는 비밀번호가 일치하지 않습니다. 다시 확인해주세요"); // 실패 시 팝업
            } else {
                alert("네트워크 오류가 발생하였습니다."); // 네트워크 오류
            }
        }
    };

    // 이메일 기억하기 핸들러
    const handleRememberCode = () => {
        setRememberCode(!rememberCode);
    };

    return (
        <div className="login-form">
            <div className="login-header">
                <h2>로그인 수정 완료</h2>
                <button
                    onClick={() => navigate("/Signup")}
                    className="register-button"
                >
                    회원가입
                </button>
            </div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="code">아이디</label>
                    <input
                        type="text"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                        placeholder="아이디를 입력하세요"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="비밀번호를 입력하세요"
                        required
                    />
                </div>
                <div>
                    <label>
                        <input
                            type="checkbox"
                            checked={rememberCode}
                            onChange={handleRememberCode}
                        />
                        아이디 기억하기
                    </label>
                </div>
                <button type="submit">로그인</button>
            </form>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
        </div>
    )
}
export default Login;