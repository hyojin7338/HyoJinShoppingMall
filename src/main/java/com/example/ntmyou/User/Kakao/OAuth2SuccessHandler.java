package com.example.ntmyou.User.Kakao;

import com.example.ntmyou.Config.JWT.JwtToken;
import com.example.ntmyou.Config.JWT.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) ((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account")).get("email");

        // JWT 토큰 발급
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 리디렉트 + 토큰 전달 (쿼리 파라미터로 보냄)
        String redirectUrl = "http://15.164.216.15/oauth-success?accessToken=" + jwtToken.getAccessToken();
        response.sendRedirect(redirectUrl);
    }

}
