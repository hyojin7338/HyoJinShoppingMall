package com.example.ntmyou.User.Kakao;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.Config.JWT.JwtToken;
import com.example.ntmyou.Config.JWT.JwtTokenProvider;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/kakao")
public class KakaoController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인 시도 시 기존 유저이면 로그인을 아니면 간편 회원가입을 진행한다.
    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@AuthenticationPrincipal OAuth2User oAuth2User) {
        // 1. 카카오 유저 정보 추출
        String kakaoId = oAuth2User.getAttribute("id").toString();
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");

        String email = (String) kakaoAccount.get("email");
        if (email == null || email.isBlank()) {
            email = "kakao_" + kakaoId + "@noemail.kakao";
        }

        String nickname = ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname").toString();

        // 2. 유저 조회 or 간편 회원가입
        Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = userRepository.save(User.builder()
                    .kakaoId(kakaoId)
                    .code(email)
                    .name(nickname)
                    .role(Role.USER)
                    .build());
        }

        // 3. Authentication 객체 수동 생성
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getCode(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // 4. JWT 토큰 발급
        JwtToken token = jwtTokenProvider.generateToken(authentication);

        // 5. 토큰 응답 // JWT 토큰 발급 후 응답
        return ResponseEntity.ok(token);
    }
}
