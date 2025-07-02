import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const KakaoRedirectHandler = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const url = new URL(window.location.href);
        const accessToken = url.searchParams.get("accessToken");
        const refreshToken = url.searchParams.get("refreshToken");
        const userInfo = url.searchParams.get("user");

        if (accessToken && refreshToken && userInfo) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            localStorage.setItem("user", userInfo);

            alert("카카오 로그인 성공!");
            navigate("/main");
        } else {
            alert("카카오 로그인 실패 또는 정보 누락");
            navigate("/login");
        }
    }, []);

    return <div>로그인 처리 중...</div>;
};

export default KakaoRedirectHandler;
