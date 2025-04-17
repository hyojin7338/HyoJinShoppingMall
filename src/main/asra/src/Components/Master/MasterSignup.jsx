import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const MasterSignup = () => {
    const [formData, setFormData] = useState({
        code: "",
        name: "",
        businessName:"",
        businessNo:"",
        password: "",
        passwordConfirm:"",
        zipCode: "",
        address: "",
        region: "",
        tel: "",
        role: "MASTER", // 기본값: 일반 회원
    });

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    // code 중복처리 START
    const [isCheckCodeFirst, setIsCheckCodeFirst] = useState(false);
    const checkCodeValid = async (e) => {
        e.preventDefault();
        if (!formData.code){
            alert("아이디을 입력해 주세요.");
            return;
        }
        try {
            const response = await axios.get(`http://15.164.216.15:8080/codeCheck?code=${formData.code}`);
            if (response.data === "SUCCESS") {
                alert("사용 가능한 아이디입니다.");
                setIsCheckCodeFirst(true);
            }
        } catch (err) {
            if (err.response && err.response.status === 409) {
                alert("이미 존재하는 아이디 입니다.")
                setIsCheckCodeFirst(false);
            } else {
                alert("아이디 확인 중 오류가 발생하였습니다.");
                console.error(err);
            }
        }
    };


    // name 중복처리 START
    const [isCheckNameFirst, setIsCheckNameFirst] = useState(false);
    const checkNameValid = async (e) => {
        e.preventDefault();
        if (!formData.name){
            alert("닉네임을 입력해 주세요.");
            return;
        }
        try {
            const response = await axios.get(`http://15.164.216.15:8080/nameCheck?name=${formData.name}`);
            if (response.data === "SUCCESS") {
                alert("사용 가능한 닉네임 입니다.");
                setIsCheckNameFirst(true);
            }
        } catch (err) {
            if (err.response && err.response.status === 409) {
                alert("이미 존재하는 아이디 입니다.");
                setIsCheckNameFirst(false);
            } else {
                alert("닉네임 확인 중 오류가 발생하였습니다. ");
                console.error(err);
            }
        }
    };


    // businessName 중복처리 START
    const [isCheckBusinessNameFirst, setIsCheckbusinessNameFirst] = useState(false);
    const checkBusinessNameValid = async (e) => {
        e.preventDefault();
        if (!formData.code){
            alert("아이디을 입력해 주세요.");
            return;
        }
        try {
            const response = await axios.get(`http://15.164.216.15:8080/codeCheck?code=${formData.code}`);
            if (response.data === "SUCCESS") {
                alert("사용 가능한 사업자명입니다.");
                setIsCheckbusinessNameFirst(true);
            }
        } catch (err) {
            if (err.response && err.response.status === 409) {
                alert("이미 존재하는 사업자명 입니다.")
                setIsCheckbusinessNameFirst(false);
            } else {
                alert("사업자명 확인 중 오류가 발생하였습니다.");
                console.error(err);
            }
        }
    };

    // businessNo 중복처리 START
    const [isCheckBusinessNoFirst, setIsCheckbusinessNoFirst] = useState(false);
    const checkBusinessNoValid = async (e) => {
        e.preventDefault();
        if (!formData.code){
            alert("사업자번호를 입력해 주세요.");
            return;
        }
        try {
            const response = await axios.get(`http://15.164.216.15:8080/codeCheck?code=${formData.code}`);
            if (response.data === "SUCCESS") {
                alert("사용 가능한 사업자번호입니다.");
                setIsCheckbusinessNoFirst(true);
            }
        } catch (err) {
            if (err.response && err.response.status === 409) {
                alert("이미 존재하는 사업자번호 입니다.")
                setIsCheckbusinessNoFirst(false);
            } else {
                alert("사업자번호 확인 중 오류가 발생하였습니다.");
                console.error(err);
            }
        }
    };


    //우편번호 찾기 다음 무료 api 사용
    useEffect(() => {
        const script = document.createElement("script");
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        script.async = true;
        document.body.appendChild(script);

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    // 입력값 변경 처리
    const handleChange = (e) => {

        const { name, value } = e.target;

        // role 필드에 대한 별도 처리 가능
        if (name === "role" && value === "") {
            setFormData({ ...formData, [name]: "Master" }); // 기본값 설정
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    // 필수 입력 값
    const handleSubmit = async (e) => {
        e.preventDefault();
        // 아이디 확인
        if (!isCheckCodeFirst) {
            alert("아이디을 확인 해 주세요!!!.");
            return;
        }

        // 닉네임 확인
        if (!isCheckNameFirst) {
            alert("닉네임을 확인 해 주세요!!!!!!!.");
            return;
        }

        // 사업자명
        if (!isCheckBusinessNameFirst) {
            alert("사업자명을 확인 해 주세요!");
            return;
        }

        // 사업자번호
        if (!isCheckBusinessNoFirst) {
            alert("사업자번호를 확인 해 주세요!!!!!!!.");
            return;
        }

        // 유효성 검사
        if (!formData.code || !formData.name || !formData.password || !formData.businessNo
            || !formData.businessName) {
            setError("필수입력 값 입니다.");
            return;
        }

        // 500 error 가 났던 부분 sDay 필드를 현재 날짜로 자동 추가하기
        const currentDate = new Date().toISOString().split('T')[0]; //현재 날짜로 채우기
        const formDataWithDate = {
            ...formData,
            sDay : currentDate
        }; // 자동으로 가입 날짜 추가

        try {
            // 서버와 통신
            // const response = await axios.post("http://15.164.216.15:8080/signUp", formData);
            await axios.post("http://15.164.216.15:8080/master/signup", formData);
            setSuccess("관리자 회원가입이 완료되었습니다!");
            setError("");
        } catch (error) {
            setError(error.response?.data?.message || "관리자 회원가입 중 오류가 발생했습니다.");
            setSuccess("");
        }
    };

    // 우편찾기 무료 api 사용
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
                    address: addr,
                    zipCode: data.zonecode,
                }));
            },
        }).open();
    }

    // 숫자만 입력 할 수 있게 ex) 전화번호, 쇼핑몰 사업자 번호
    const handleNumberInput  = (e) => {
        const {name, value} = e.target;

        // 숫자가 아닌 문자를 제거
        const numberDelete = value.replace(/[^0-9]/g, "");

        setFormData((preData) => ({
            ...preData,
            [name]: numberDelete, // 숫자만 설정
        }));
    }

    // 비밀번호 검증 함수
    const validatePassword = () => {
        if (formData.password !== formData.passwordConfirm) {
            setError("비밀번호가 일치하지 않습니다.");
            return false;
        }
        setError(""); // 오류 메시지 초기화
        return true;
    };

    return (
        <div className="signup-form">
            <div className="signup-header">
            <h2>회원가입</h2>
                <button
                    onClick={() => navigate("/login")}
                    className="login-button"
                >
                    로그인
                </button>
            </div>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {success && <p style={{ color: "green" }}>{success}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>아이디</label>
                    <input
                        type="text"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                        required
                    />
                    <button type="button" onClick={checkCodeValid}>중복 확인</button>
                </div>
                <div>
                    <label>닉네임</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                    <button type="button" onClick={checkNameValid}>중복 확인</button>
                </div>
                <div>
                    <label>사업자명</label>
                    <input
                        type="text"
                        name="businessName"
                        value={formData.businessName}
                        onChange={handleChange}
                        required
                    />
                    <button type="button" onClick={checkBusinessNameValid}>중복 확인</button>
                </div>
                <div>
                    <label>사업자번호</label>
                    <input
                        type="text"
                        name="businessNo"
                        value={formData.businessNo}
                        onChange={handleNumberInput}
                        required
                    />
                    <button type="button" onClick={checkBusinessNoValid}>중복 확인</button>
                </div>
                <div>
                    <label>비밀번호</label>
                    <input type="password"
                           name="password"
                           value={formData.password}
                           onChange={handleChange} required />
                </div>

                <div>
                    <label>비밀번호 확인</label>
                    <input
                        type="password"
                        name="passwordConfirm"
                        value={formData.passwordConfirm}
                        onChange={handleChange}
                        onBlur={validatePassword} // 포커스가 벗어날 때 검증
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
                        placeholder="상세 주소를 입력하세요" // 힌트 추가
                    />
                </div>
                <div>
                    <label>우편번호</label>
                    <input
                        type="text"
                        name="zipCode"
                        value={formData.zipCode}
                        onChange={handleNumberInput}
                        readOnly
                    />
                </div>
                <div>
                    <label>전화번호</label>
                    <input
                        type="tel"
                        name="tel"
                        value={formData.tel}
                        onChange={handleNumberInput}
                    />
                </div>
                <div>
                    <label>역할</label>
                    <select name="role" value={formData.role} onChange={handleChange}>
                        <option value="MASTER">관리자 회원</option>
                    </select>
                </div>
                <button type="submit">회원가입</button>
            </form>
        </div>
    );
};

export default MasterSignup;
