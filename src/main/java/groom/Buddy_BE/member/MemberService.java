package groom.Buddy_BE.member;

import groom.Buddy_BE.kakao.KakaoUserInfoResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}