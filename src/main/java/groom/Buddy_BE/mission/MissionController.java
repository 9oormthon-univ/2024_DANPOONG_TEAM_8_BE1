package groom.Buddy_BE.mission;

import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
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
    private final MemberService memberService;

    // 진행 중인 미션 조회 API
    @GetMapping("/ongoing")
    public ResponseEntity<List<MissionResponseDTO>> getOngoingMissions(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 토큰에서 멤버 조회
            String token = authorizationHeader.replace("Bearer ", "");
            Member member = memberService.findMemberByToken(token);

            // 2. 진행 중인 미션 조회
            List<MissionResponseDTO> ongoingMissions = missionService.getOngoingMissionsByKakaoId(member.getKakaoId());

            return ResponseEntity.ok(ongoingMissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 완료된 미션 조회 API
    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedMissions(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 토큰에서 멤버 조회
            String token = authorizationHeader.replace("Bearer ", "");
            Member member = memberService.findMemberByToken(token);

            // 2. 완료된 미션 조회
            List<MissionResponse2DTO> completedMissions = missionService.getCompletedMissionsByKakaoId(member.getKakaoId());

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

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }
    }
}
