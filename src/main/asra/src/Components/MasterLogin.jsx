import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import "../styles/Login.css"

const MasterLogin = () => {
    const [formData, setFormData] = useState({code: "", password: "",});
    const [rememberCode, setRememberCode] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    // 로컬 스토리지에서 저장 된 이메일 불러오기
    useEffect(() => {
        const saveCode = localStorage.getItem("saveCode");
        if (saveCode){
            setFormData((prev) => ({ ...prev, code : saveCode }));
            setRememberCode(true);
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
            setError("이메일과 비밀번호를 입력해주세요. ");
            return;
        }

        try {
            const response = await axios.post("http://localhost:8080/master/login", formData);
            const { accessToken, refreshToken } = response.data;

            // 이메일 기억하기
            if (rememberCode) {
                localStorage.setItem("savedCode", formData.code);
            } else {
                localStorage.removeItem("savedCode");
            }

            // JWT 토큰 저장
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            setSuccess("로그인 성공적으로 완료되었습니다.");
            setError("");
        } catch (err) {
            if (err.response) {
                setError(err.response.data || "로그인에 실패하였습니다.");
            } else {
                setError("네트워크 오류가 발생하였습니다.");
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
                <h2>관리자 로그인</h2>
                <button
                    onClick={() => navigate("/MasterSignup")}
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
export default MasterLogin;