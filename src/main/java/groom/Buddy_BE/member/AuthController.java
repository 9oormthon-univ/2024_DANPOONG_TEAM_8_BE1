package groom.Buddy_BE.member;

import groom.Buddy_BE.kakao.KakaoService;
import groom.Buddy_BE.kakao.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private final JwtProvider jwtProvider;

    @Autowired
    private MemberService memberService;

@Autowired
private MemberRepository memberRepository;

    public AuthController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
        // 1. Access Token 받아오기
        String accessToken = kakaoService.getAccessToken(code);

        // 2. 사용자 정보 받아오기
        KakaoUserInfoResponseDto kakaoUserInfo = kakaoService.getUserInfo(accessToken);

        // 3. 사용자 정보 저장
        memberService.saveOrUpdateMember(kakaoUserInfo);

        return ResponseEntity.ok("로그인 및 저장 성공");
    }

    //토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            // 1. 리프레시 토큰 검증
            if (!jwtProvider.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 유효하지 않습니다.");
            }

            // 2. 토큰에서 사용자 ID 추출
            Long memberId = jwtProvider.getUserIdFromToken(refreshToken);

            // 3. 데이터베이스에서 사용자 조회
            Member member = memberService.findById(memberId);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            // 4. 새로운 액세스 토큰 및 리프레시 토큰 생성
            String newAccessToken = jwtProvider.createAccessToken(member.getId());
            String newRefreshToken = jwtProvider.createRefreshToken(member.getId());

            // 5. 응답 데이터 구성
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);

            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 재발급 중 오류가 발생했습니다.");
        }
    }
}