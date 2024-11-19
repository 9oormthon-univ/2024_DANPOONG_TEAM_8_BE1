package groom.Buddy_BE.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;

    private final String KAUTH_TOKEN_URL = "https://kauth.kakao.com";
    private final String KAPI_USER_URL = "https://kapi.kakao.com";

    public String getAccessToken(String code) {
        return WebClient.create(KAUTH_TOKEN_URL)
                .post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block()
                .getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        return WebClient.create(KAPI_USER_URL)
                .get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
    }
}