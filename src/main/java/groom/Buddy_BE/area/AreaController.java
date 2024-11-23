package groom.Buddy_BE.area;

import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;
    private final MemberService memberService;

    // 영역 생성
    @PostMapping("/create")
    public ResponseEntity<?> createArea(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AreaRequestDTO requestDTO) {

        // JWT 토큰으로 멤버 조회
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 2. 영역 생성 및 설정
        Area.AreaType areaType;
        try {
            areaType = Area.AreaType.valueOf(requestDTO.getAreaType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("유효하지 않은 영역 타입입니다.", HttpStatus.BAD_REQUEST);
        }

        Area area = areaService.createArea(areaType);
        area.setMember(member);
        member.getAreas().add(area);

        // 3. 멤버 정보 업데이트
        memberService.save(member);

        // 4. 응답 DTO
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setId(member.getId());
        memberInfoDTO.setNickname(member.getNickname());
        memberInfoDTO.setKakaoId(member.getKakaoId());

        AreaResponseDTO responseDTO = new AreaResponseDTO();
        responseDTO.setId(area.getId());
        responseDTO.setAreaType(area.getAreaType().name());
        responseDTO.setMember(memberInfoDTO);

        return ResponseEntity.ok(responseDTO);
    }

    // 미션 - 자립 목표 페이지
    @GetMapping("/home")
    public ResponseEntity<?> homeArea(@RequestHeader("Authorization") String authorizationHeader) {
        // JWT 토큰으로 멤버 조회
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        if (member.getCharacter() == null) {
            return new ResponseEntity<>("캐릭터가 생성되지 않았습니다.", HttpStatus.NOT_FOUND);
        }

        String progressAreaType = areaService.progressAreaType(member.getKakaoId());
        if (progressAreaType.isEmpty()) {
            progressAreaType = "ALL";
        }

        double progressPercentage = areaService.progressPercentage(member.getKakaoId());

        List<String> areaTypes = areaService.completeAreaTypes(member.getKakaoId());

        AreaHomeResponseDTO areaHomeResponseDTO = new AreaHomeResponseDTO();
        areaHomeResponseDTO.setProgressAreaType(progressAreaType);
        areaHomeResponseDTO.setPercentage(progressPercentage);
        areaHomeResponseDTO.setCompleteAreaTypes(areaTypes);

        return ResponseEntity.ok(areaHomeResponseDTO);
    }

    // 영역 완수 후 넘어가는 새로운 영역 생성 페이지
    @GetMapping("/next/create")
    public ResponseEntity<?> nextCreateArea(@RequestHeader("Authorization") String authorizationHeader) {
        // JWT 토큰으로 멤버 조회
        String token = authorizationHeader.replace("Bearer ", "");
        Member member = memberService.findMemberByToken(token);

        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        if (member.getAreas() == null || member.getAreas().isEmpty()) {
            return new ResponseEntity<>("완수한 영역이 없습니다.", HttpStatus.NOT_FOUND);
        }

        // 해당 유저의 영역 타입 리스트 가져오기
        List<String> areaTypes = areaService.completeAreaTypes(member.getKakaoId());

        if (areaTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("완수한 영역이 없습니다.");
        }

        // 영역 타입 리스트 반환
        return ResponseEntity.ok(areaTypes);
    }
}
