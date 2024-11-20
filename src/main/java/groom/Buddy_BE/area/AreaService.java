package groom.Buddy_BE.area;

import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;
    private final MemberService memberService;

    //영역 설정
    public Area createArea(Area.AreaType areaType) {
        Area area = new Area();
        area.setAreaType(areaType);

        areaRepository.save(area);

        return area;
    }

    // ID로 Area 조회하는 메서드
    public Area findById(Long areaId) {
        Optional<Area> area = areaRepository.findById(areaId);
        return area.orElse(null);  // 존재하지 않으면 null 반환
    }

    //영역 완수 여부 - 완료로 변경
    public Area completeArea(Long areaId) {

        //영역에 매핑되어있는 미션들이 다 완수가 되었는지

        Area area = findById(areaId);

        area.setCompleted(true);
        areaRepository.save(area);

        return area;
    }


    //수행 완료한 영역 리스트 출력
    public List<String> completeAreaTypes(Long kakaoId) {
        Member member = memberService.findByKakaoId(kakaoId);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        // 해당 유저의 영역 중 isCompleted가 true인 영역의 AreaType 추출
        return member.getAreas().stream()
                .filter(Area::isCompleted) // isCompleted가 true인 영역만 필터링
                .map(area -> area.getAreaType().name()) // AreaType을 문자열로 변환
                .collect(Collectors.toList());
    }

    //수행 중인 영역 출력
    public String progressAreaType(Long kakaoId) {
        Member member = memberService.findByKakaoId(kakaoId);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        // 수행 중인 영역의 areaType 반환 (isCompleted가 false인 첫 번째 영역)
        return member.getAreas().stream()
                .filter(area -> !area.isCompleted()) // isCompleted가 false인 영역 필터링
                .map(area -> area.getAreaType().name()) // AreaType을 문자열로 변환
                .findFirst() // 첫 번째 영역 반환
                .orElse(null); // 없을 경우 null 반환
    }

    // 현재 수행 중인 영역의 미션 수행률 계산
    public double progressPercentage(Long kakaoId) {
        Member member = memberService.findByKakaoId(kakaoId);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        // 수행 중인 영역 찾기
        Area progressArea = member.getAreas().stream()
                .filter(area -> !area.isCompleted()) // 수행 중인 영역 필터링
                .findFirst() // 첫 번째 영역 가져오기
                .orElse(null);

        if (progressArea == null) {
            return 0.0; // 수행 중인 영역이 없으면 0.0 반환
        }

        // 완료된 미션 개수
        long completedMissions = progressArea.getMissions().stream()
                .filter(mission -> mission.isCompleted()) // 완료된 미션 필터링
                .count();

        // 미션 개수는 5개로 고정
        int missionTotal = 5;
        return (double) completedMissions / missionTotal * 100; // 수행률 반환
    }
}
