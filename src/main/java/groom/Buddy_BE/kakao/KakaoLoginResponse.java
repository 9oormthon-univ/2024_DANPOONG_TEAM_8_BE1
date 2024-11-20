package groom.Buddy_BE.kakao;

import lombok.Data;

@Data
class KakaoLoginResponse {
    private String message;
    private boolean hasCharacter;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public KakaoLoginResponse(String message, boolean hasCharacter, String nickname, String accessToken, String refreshToken) {
        this.message = message;
        this.hasCharacter = hasCharacter;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}