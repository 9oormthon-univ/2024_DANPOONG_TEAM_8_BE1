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
}
