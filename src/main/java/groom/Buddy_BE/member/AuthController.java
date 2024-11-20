package groom.Buddy_BE.member;

import groom.Buddy_BE.kakao.KakaoService;
import groom.Buddy_BE.kakao.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private MemberService memberService;

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
}