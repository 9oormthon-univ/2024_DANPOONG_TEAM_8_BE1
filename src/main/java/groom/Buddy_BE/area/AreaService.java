package groom.Buddy_BE.area;


import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
