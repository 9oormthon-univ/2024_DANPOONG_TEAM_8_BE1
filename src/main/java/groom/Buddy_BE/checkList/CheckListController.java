package groom.Buddy_BE.checkList;

import groom.Buddy_BE.area.Area;
import groom.Buddy_BE.area.AreaInfoDTO;
import groom.Buddy_BE.area.AreaService;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkList")
@RequiredArgsConstructor
public class CheckListController {

    private final MemberService memberService;
    private final AreaService areaService;
    private final CheckService checkService;

    // 사전 점검표 생성
    @PostMapping("/create")
    public ResponseEntity<?> createCheckList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("areaId") Long areaId,  // 영역 ID를 파라미터로 받음
            @RequestBody CheckListRequestDTO requestDTO) {

        // 1. 토큰을 통해 멤버 매핑
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 2. 영역 매핑
        Area area = areaService.findById(areaId);
        if (area == null || !member.getAreas().contains(area)) {
            return new ResponseEntity<>("해당 영역이 존재하지 않거나, 회원이 접근할 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        // 3. 사전 점검표 생성
        CheckList checkList = checkService.createCheckList(
                requestDTO.getFistQ(),
                requestDTO.getSecondQ(),
                requestDTO.getThirdQ(),
                requestDTO.getFourthQ(),
                requestDTO.getFifthQ(),
                requestDTO.getSixthQ(),
                requestDTO.getSeventhQ(),
                requestDTO.getEighthQ());

        // 4. 사전 점검표에 영역 및 멤버 매핑
        checkList.setArea(area);
        checkList.setMember(member);

        // 5. 사전 점검표를 CheckListRepository를 통해 직접 저장
        checkService.saveCheckList(checkList);

        // 6. 응답용 DTO 생성
        AreaInfoDTO areaInfoDTO = new AreaInfoDTO(area.getId(), area.getAreaType().name());
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();

        memberInfoDTO.setId(member.getId());
        memberInfoDTO.setNickname(member.getNickname());
        memberInfoDTO.setKakaoId(member.getKakaoId());

        CheckListResponseDTO responseDTO = new CheckListResponseDTO();
        responseDTO.setId(checkList.getId());
        responseDTO.setFistQ(checkList.getFistQ());
        responseDTO.setSecondQ(checkList.getSecondQ());
        responseDTO.setThirdQ(checkList.getThirdQ());
        responseDTO.setFourthQ(checkList.getFourthQ());
        responseDTO.setFifthQ(checkList.getFifthQ());
        responseDTO.setSixthQ(checkList.getSixthQ());
        responseDTO.setSeventhQ(checkList.getSeventhQ());
        responseDTO.setEighthQ(checkList.getEighthQ());

        responseDTO.setArea(areaInfoDTO);
        responseDTO.setMember(memberInfoDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
