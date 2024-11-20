package groom.Buddy_BE.mission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    // 특정 회원의 진행 중인 미션 DTO 반환
    public List<MissionResponseDTO> getOngoingMissionsByKakaoId(Long kakaoId) {
        return missionRepository.findOngoingMissionsByKakaoId(kakaoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 회원의 완료된 미션 DTO 반환
    public List<MissionResponseDTO> getCompletedMissionsByKakaoId(Long kakaoId) {
        return missionRepository.findCompletedMissionsByKakaoId(kakaoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Mission 엔티티를 DTO로 변환
    private MissionResponseDTO convertToDTO(Mission mission) {
        MissionResponseDTO dto = new MissionResponseDTO();
        dto.setId(mission.getId());
        dto.setMissionName(mission.getMission_name());
        dto.setAreaName(mission.getArea().getAreaType().name());
        return dto;
    }



}
