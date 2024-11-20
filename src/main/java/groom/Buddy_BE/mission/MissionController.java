package groom.Buddy_BE.mission;

import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity<?> getCompletedMissions(@RequestHeader("kakaoId") Long kakaoId) {
        List<MissionResponse2DTO> completedMissions = missionService.getCompletedMissionsByKakaoId(kakaoId);

        if (completedMissions == null || completedMissions.isEmpty()) {
            // 빈 배열과 메시지를 함께 반환
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "완료한 미션이 존재하지 않습니다.");
                put("missions", new ArrayList<>()); // 빈 리스트 반환
            }});
        }

        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("missions", completedMissions);
        }});

    }
}

