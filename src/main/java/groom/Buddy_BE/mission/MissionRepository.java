package groom.Buddy_BE.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    // 진행 중인 미션 조회
    @Query("SELECT mission FROM Mission mission WHERE mission.member.kakaoId = :kakaoId AND mission.isCompleted = false")
    List<Mission> findOngoingMissionsByKakaoId(@Param("kakaoId") Long kakaoId);

    // 완료된 미션 조회
    @Query("SELECT mission FROM Mission mission WHERE mission.member.kakaoId = :kakaoId AND mission.isCompleted = true")
    List<Mission> findCompletedMissionsByKakaoId(@Param("kakaoId") Long kakaoId);
}


