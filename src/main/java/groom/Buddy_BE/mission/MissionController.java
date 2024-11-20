package groom.Buddy_BE.mission;

import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 진행 중인 미션 조회 API
    @GetMapping("/ongoing")
    public ResponseEntity<List<MissionResponseDTO>> getOngoingMissions(@RequestHeader("kakaoId") Long kakaoId) {

        List<MissionResponseDTO> ongoingMissions = missionService.getOngoingMissionsByKakaoId(kakaoId);

        return ResponseEntity.ok(ongoingMissions);
    }

    // 완료된 미션 조회 API
    @GetMapping("/completed")
    public ResponseEntity<List<MissionResponseDTO>> getCompletedMissions(@RequestHeader("kakaoId") Long kakaoId) {
        List<MissionResponseDTO> completedMissions = missionService.getCompletedMissionsByKakaoId(kakaoId);
        return ResponseEntity.ok(completedMissions);
    }
}
