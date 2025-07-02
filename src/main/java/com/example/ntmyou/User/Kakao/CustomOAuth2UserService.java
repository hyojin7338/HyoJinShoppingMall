package com.example.ntmyou.User.Kakao;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String kakaoId = oAuth2User.getAttribute("id").toString();
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String nickname = ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname").toString();

        if (email == null || email.isBlank()) {
            email = "kakao_" + kakaoId + "@noemail.kakao";
        }

        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (user == null) {
            user = userRepository.save(User.builder()
                    .kakaoId(kakaoId)
                    .code(email)
                    .name(nickname)
                    .role(Role.USER)
                    .build());
        }
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(),
                "id" // 기본 식별자 attribute 키
        );
    }
}
