package groom.Buddy_BE.kakao;

import groom.Buddy_BE.member.JwtProvider;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;  // JWT 토큰을 생성하는 서비스

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam("code") String code) {
        try {
            // Access Token 받아오기
            String kakaoAccessToken = kakaoService.getAccessToken(code);

            // 사용자 정보 가져오기
            KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);

            // 사용자 저장 및 업데이트
            Member member = memberService.saveOrUpdateMember(userInfo);

            // JWT 토큰 생성
            String jwtAccessToken = jwtProvider.createAccessToken(member.getId());
            String jwtRefreshToken = jwtProvider.createRefreshToken(member.getId());

            // 캐릭터 존재 여부 판단
            boolean hasCharacter = member.getCharacter() != null;

            // 응답 데이터 생성
            KakaoLoginResponse response = new KakaoLoginResponse(
                    "로그인 성공",
                    hasCharacter,
                    member.getNickname(),
                    jwtAccessToken,
                    jwtRefreshToken
            );

            // 리다이렉트 URL 설정 (query parameters 포함)
            String redirectUrl = String.format("http://localhost:5173/auth?token=%s&refreshToken=%s&nickname=%s&hasCharacter=%b&kakaoId=%s",
                    jwtAccessToken,
                    jwtRefreshToken,
                    URLEncoder.encode(member.getNickname(), StandardCharsets.UTF_8.toString()),
                    hasCharacter,
                    URLEncoder.encode(member.getKakaoId().toString(), StandardCharsets.UTF_8.toString())
            );
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(redirectUrl);
            return redirectView;

        } catch (Exception e) {
            log.error("Error during Kakao login process", e);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://localhost:5173/auth?error=login_failed");
            return redirectView;
        }
    }

    //헤더 토큰 테스트
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        // 로그에 Authorization 헤더 출력
        log.info("Authorization Header: {}", authorizationHeader);

        // Bearer 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");
        log.info("Token: {}", token);

        // 필요한 로직 수행
        return ResponseEntity.ok("Token received successfully");
    }
}

