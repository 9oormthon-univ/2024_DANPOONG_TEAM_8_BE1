package groom.Buddy_BE.missionRecord;

import groom.Buddy_BE.area.Area;
import groom.Buddy_BE.area.AreaService;
import groom.Buddy_BE.character.Character;
import groom.Buddy_BE.character.CharacterResponseDTOForMission;
import groom.Buddy_BE.character.CharacterService;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.member.MemberService;
import groom.Buddy_BE.mission.Mission;
import groom.Buddy_BE.mission.MissionRepository;
import groom.Buddy_BE.mission.MissionResponse2DTO;
import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/missionRecord")
@RequiredArgsConstructor
public class MissionRecordController {
    private final MissionRecordService missionRecordService;
    private final MemberService memberService;
    private final MissionRepository missionRepository;
    private final CharacterService characterService;
    private final AreaService areaService;

    @PostMapping("/create")
    public ResponseEntity<?> createMissionRecord(
            @RequestParam("mission") Long missionId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody MissionRecordRequestDTO missionRecordRequestDTO) {

        // 1. 토큰에서 멤버 조회
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        // 2. 미션 조회
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission == null) {
            return new ResponseEntity<>("미션이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 3. 미션 기록 생성
        MissionRecord missionRecord = missionRecordService.createMissionRecord(
                missionRecordRequestDTO.getContent(),
                missionRecordRequestDTO.getFeedback());
        missionRecord.setMission(mission);
        missionRecord.setMember(member);

        // 4. 미션 완료 상태 업데이트
        mission.setCompleted(true);
        member.getMissionRecords().add(missionRecord);
        memberService.save(member);

        // 5. 미션이 속한 영역 가져오기
        Area area = mission.getArea();

        // 6. 영역 내 모든 미션의 완료 여부 확인
        boolean allCompleted = area.getMissions().stream()
                .allMatch(Mission::isCompleted);

        // 7. 모든 미션이 완료되었을 경우 캐릭터의 레벨을 1 증가
        if (allCompleted) {
            areaService.completeArea(area.getId());

            Character character = member.getCharacter();
            if (character != null) {
                characterService.levelUpCharacter(member.getKakaoId());
            }
        }

        // 8. 응답 DTO 생성
        MissionResponse2DTO missionResponseDTO = new MissionResponse2DTO();
        missionResponseDTO.setId(mission.getId());
        missionResponseDTO.setMissionName(mission.getMission_name());
        missionResponseDTO.setAreaName(area.getAreaType().name());
        missionResponseDTO.setCompleted(mission.isCompleted());


        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setId(member.getId());
        memberInfoDTO.setKakaoId(member.getKakaoId());
        memberInfoDTO.setNickname(member.getNickname());

        CharacterResponseDTOForMission characterResponseDTO = new CharacterResponseDTOForMission();
        characterResponseDTO.setId(member.getCharacter().getId());
        characterResponseDTO.setCharacterType(member.getCharacter().getCharacterType().toString());
        characterResponseDTO.setCharacterName(member.getCharacter().getCharacterName());
        characterResponseDTO.setLevel(member.getCharacter().getLevel());

        MissionRecordResponseDTO missionRecordResponseDTO = new MissionRecordResponseDTO();
        missionRecordResponseDTO.setId(missionRecord.getId());
        missionRecordResponseDTO.setContent(missionRecord.getContent());
        missionRecordResponseDTO.setFeedback(missionRecord.getFeedback());
        missionRecordResponseDTO.setMissionResponse2DTO(missionResponseDTO);
        missionRecordResponseDTO.setMemberInfoDTO(memberInfoDTO);
        missionRecordResponseDTO.setAllCompleted(allCompleted);
        missionRecordResponseDTO.setCharacterResponseDTOForMission(characterResponseDTO);

        return new ResponseEntity<>(missionRecordResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/create")
    public ResponseEntity<?> getMissionRecords_page(
            @RequestParam("mission") Long missionId,
            @RequestHeader("Authorization") String authorizationHeader) {

        // 1. 토큰에서 멤버 조회
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        // 2. 미션 조회
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission == null) {
            return new ResponseEntity<>("미션이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 3. 응답 DTO 생성
        MissionResponseDTO missionResponseDTO = new MissionResponseDTO();
        missionResponseDTO.setId(mission.getId());
        missionResponseDTO.setMissionName(mission.getMission_name());
        missionResponseDTO.setAreaName(mission.getArea().getAreaType().name());

        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setId(member.getId());
        memberInfoDTO.setNickname(member.getNickname());
        memberInfoDTO.setKakaoId(member.getKakaoId());

        return new ResponseEntity<>(new HashMap<String, Object>() {{
            put("mission", missionResponseDTO);
            put("member", memberInfoDTO);
        }}, HttpStatus.OK);
    }
}
