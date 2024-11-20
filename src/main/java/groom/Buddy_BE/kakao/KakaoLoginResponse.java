package groom.Buddy_BE.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoLoginResponse {
    private String message;        // 로그인 결과 메시지
    private boolean hasCharacter;  // 캐릭터가 있는지 여부
    private String nickname;       // 유저 닉네임
}