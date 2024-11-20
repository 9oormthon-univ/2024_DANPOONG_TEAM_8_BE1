package groom.Buddy_BE.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void join(Member member) {
        em.persist(member);
        em.flush(); // 데이터베이스에 즉시 반영
    }

    public Member findByKakaoId(Long kakaoId) {
        return em.createQuery("SELECT member FROM Member member WHERE member.kakaoId = :kakaoId", Member.class)
                .setParameter("kakaoId", kakaoId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); // Optional로 감싸서 반환
    }
}