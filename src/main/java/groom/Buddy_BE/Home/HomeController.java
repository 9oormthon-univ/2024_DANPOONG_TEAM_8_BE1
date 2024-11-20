package groom.Buddy_BE.Home;

import groom.Buddy_BE.area.AreaService;
import groom.Buddy_BE.character.CharacterResponseDTOForMission;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.member.MemberService;
import groom.Buddy_BE.mission.Mission;
import groom.Buddy_BE.mission.MissionResponse2DTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final AreaService areaService;

    //홈 화면
    @GetMapping("")
    public ResponseEntity<?> home(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 토큰에서 멤버 조회
            String token = authorizationHeader.replace("Bearer ", "");
            Member member = memberService.findMemberByToken(token);

            // 2. 유저 닉네임 가져오기
            String memberName = member.getNickname();

            // 3. 현재 진행 중인 영역의 미션 수행률 가져오기
            Double missionProgressPercentage = areaService.progressPercentage(member.getKakaoId());

            // 4. 유저 캐릭터 정보 가져오기
            CharacterResponseDTOForMission characterDTO = null;
            if (member.getCharacter() != null) {
                characterDTO = new CharacterResponseDTOForMission();
                characterDTO.setId(member.getCharacter().getId());
                characterDTO.setCharacterType(member.getCharacter().getCharacterType().toString());
                characterDTO.setCharacterName(member.getCharacter().getCharacterName());
                characterDTO.setLevel(member.getCharacter().getLevel());
            }

            // 5. 진행 중인 첫 번째 미션 가져오기
            Mission firstMission = member.getMissions().stream()
                    .filter(mission -> !mission.isCompleted())
                    .findFirst()
                    .orElse(null);

            MissionResponse2DTO missionDTO = null;
            if (firstMission != null) {
                missionDTO = new MissionResponse2DTO();
                missionDTO.setId(firstMission.getId());
                missionDTO.setMissionName(firstMission.getMission_name());
                missionDTO.setAreaName(firstMission.getArea().getAreaType().name());
                missionDTO.setCompleted(firstMission.isCompleted());
            }

            // 6. 응답 DTO 생성
            HomeResponseDTO responseDTO = new HomeResponseDTO();
            responseDTO.setMember_nickname(memberName);
            responseDTO.setMissionProPer(missionProgressPercentage);
            responseDTO.setCharacter(characterDTO);
            responseDTO.setMission(missionDTO);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    //마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 토큰에서 멤버 조회
            String token = authorizationHeader.replace("Bearer ", "");
            Member member = memberService.findMemberByToken(token);

            // 2. 멤버 정보 DTO 생성
            MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
            memberInfoDTO.setNickname(member.getNickname());
            memberInfoDTO.setKakaoId(member.getKakaoId());
            memberInfoDTO.setId(member.getId());

            return ResponseEntity.ok(memberInfoDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
