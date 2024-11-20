package groom.Buddy_BE.area;


import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;
    private final MemberService memberService;

    //영역 생성
    @PostMapping("/create")
    public ResponseEntity<?> createArea(
            @RequestHeader("kakaoId") Long kakaoId,
            @RequestBody AreaRequestDTO requestDTO) {

        // 1. kakaoId로 멤버 조회
        Member member = memberService.findByKakaoId(kakaoId);
        if (member == null) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // 2. 영역 생성 및 설정
        Area.AreaType areaType;
        try {
            areaType = Area.AreaType.valueOf(requestDTO.getAreaType().toUpperCase()); // 문자열 -> enum 변환
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("유효하지 않은 영역 타입입니다.", HttpStatus.BAD_REQUEST);
        }

        Area area = areaService.createArea(areaType);
        area.setMember(member);
        member.getAreas().add(area);

        // 3. 멤버 정보 업데이트
        memberService.save(member);

        //4. 응담 DTO
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setId(member.getId());
        memberInfoDTO.setNickname(member.getNickname());
        memberInfoDTO.setKakaoId(member.getKakaoId());

        AreaResponseDTO responseDTO = new AreaResponseDTO();
        responseDTO.setId(area.getId());
        responseDTO.setAreaType(area.getAreaType().name());
        responseDTO.setMember(memberInfoDTO); // Member 관련 정보 추가

        return ResponseEntity.ok(responseDTO);
    }

    //미션 - 자립 목표 페이지
    @GetMapping("/home")
    public ResponseEntity<?> homeArea(
            @RequestHeader("kakaoId") Long kakaoId){

        String progressAreaType = areaService.progressAreaType(kakaoId);
        if (progressAreaType.isEmpty()) {
            progressAreaType = "진행 중인 영역이 없습니다.";
        }

        double progressPercentage = areaService.progressPercentage(kakaoId);

        List<String> areaTypes = areaService.completeAreaTypes(kakaoId);

        AreaHomeResponseDTO areaHomeResponseDTO = new AreaHomeResponseDTO();
        areaHomeResponseDTO.setProgressAreaType(progressAreaType);
        areaHomeResponseDTO.setPercentage(progressPercentage);
        areaHomeResponseDTO.setCompleteAreaTypes(areaTypes);

        return ResponseEntity.ok(areaHomeResponseDTO);
    }

    // 영역 완수 후 넘어가는 새로운 영역 생성 페이지
    @GetMapping("/next/create")
    public ResponseEntity<?> nextCreateArea(
            @RequestHeader("kakaoId") Long kakaoId
    ) {
        // 해당 유저의 영역 타입 리스트 가져오기
        List<String> areaTypes = areaService.completeAreaTypes(kakaoId);

        if (areaTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("생성된 영역이 없습니다.");
        }

        // 영역 타입 리스트 반환
        return ResponseEntity.ok(areaTypes);
    }


}
