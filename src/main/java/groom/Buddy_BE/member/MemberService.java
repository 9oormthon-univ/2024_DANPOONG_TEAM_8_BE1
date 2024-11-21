package groom.Buddy_BE.member;

import groom.Buddy_BE.kakao.KakaoUserInfoResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class MemberService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public Member saveOrUpdateMember(KakaoUserInfoResponseDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        Member existingMember = memberRepository.findByKakaoId(kakaoId);

        if (existingMember != null) {
            return existingMember;
        }

        Member newMember = new Member();
        newMember.setKakaoId(kakaoId);
        newMember.setNickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickName());
        newMember.setProfileImageUrl(kakaoUserInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        memberRepository.join(newMember); // 기존의 join 메서드 사용
        return newMember;
    }

    @Transactional
    public Member findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    @Transactional
    public Member save(Member member) {
        memberRepository.join(member);
        return member;
    }

    public Member findMemberByToken(String jwtToken) {
        // 1. 토큰 유효성 검증
        if (!jwtProvider.validateToken(jwtToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다."); // 커스텀 예외로 변경 가능
        }

        // 2. 토큰에서 사용자 ID 추출
        Long memberId = jwtProvider.getUserIdFromToken(jwtToken);

        // 3. 데이터베이스에서 멤버 조회
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")); // 커스텀 예외로 변경 가능
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

}