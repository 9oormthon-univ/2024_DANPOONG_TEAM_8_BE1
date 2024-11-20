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
import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader("kakaoId") Long kakaoId,
            @RequestBody MissionRecordRequestDTO missionRecordRequestDTO) {

        // 회원 조회
        Member member = memberService.findByKakaoId(kakaoId);
        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 미션 조회
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission == null) {
            return new ResponseEntity<>("미션이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 미션 기록 생성
        MissionRecord missionRecord = missionRecordService.createMissionRecord(
                missionRecordRequestDTO.getContent(),
                missionRecordRequestDTO.getFeedback());
        missionRecord.setMission(mission);
        missionRecord.setMember(member);

        // 미션 완료 상태 업데이트
        mission.setCompleted(true);

        member.getMissionRecords().add(missionRecord);
        memberService.save(member);

        // 미션이 속한 영역 가져오기
        Area area = mission.getArea();

        // 영역 내 모든 미션의 완료 여부 확인
        boolean allCompleted = area.getMissions().stream()
                .allMatch(Mission::isCompleted); // 모든 미션의 완료 상태가 true인지 확인


        // 모든 미션이 완료되었을 경우 캐릭터의 레벨을 1 증가
        if (allCompleted) {

            //해당 영역 완료 처리하기
            areaService.completeArea(area.getId());

            //유저의 캐릭터 레벨 1 증가
            Character character = member.getCharacter();
            if (character != null) {
                characterService.levelUpCharacter(kakaoId);
            }
        }

        // 응답 DTO 생성
        MissionResponseDTO missionResponseDTO = new MissionResponseDTO();
        missionResponseDTO.setMissionName(mission.getMission_name());
        missionResponseDTO.setAreaName(area.getAreaType().name());
        missionResponseDTO.setCompleted(mission.isCompleted()); // 완료 여부 설정

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
        missionRecordResponseDTO.setMissionResponseDTO(missionResponseDTO);
        missionRecordResponseDTO.setMemberInfoDTO(memberInfoDTO);
        missionRecordResponseDTO.setAllCompleted(allCompleted); // 모든 미션 완료 여부 설정
        missionRecordResponseDTO.setCharacterResponseDTOForMission(characterResponseDTO);

        return new ResponseEntity<>(missionRecordResponseDTO, HttpStatus.CREATED);
    }

    //미션 기록 페이지 - 조회
    @GetMapping("/create")
    public ResponseEntity<?> getMissionRecords_page(
            @RequestParam("mission") Long missionId){ //파라미터로 미션 id 받아옴

        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission == null) {
            return new ResponseEntity<>("미션이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //응답 dto
        MissionResponseDTO missionResponseDTO = new MissionResponseDTO();
        missionResponseDTO.setId(mission.getId());
        missionResponseDTO.setMissionName(mission.getMission_name());
        missionResponseDTO.setAreaName(mission.getArea().getAreaType().name());

        return new ResponseEntity<>(missionResponseDTO, HttpStatus.OK);
    }

}
