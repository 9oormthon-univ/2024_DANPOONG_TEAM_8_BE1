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
    public List<MissionResponse2DTO> getCompletedMissionsByKakaoId(Long kakaoId) {
        return missionRepository.findCompletedMissionsByKakaoId(kakaoId)
                .stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }

    // Mission 엔티티를 DTO로 변환
    private MissionResponseDTO convertToDTO(Mission mission) {
        MissionResponseDTO dto = new MissionResponseDTO();
        dto.setId(mission.getId());
        dto.setMissionName(mission.getMission_name());
        dto.setAreaName(mission.getArea().getAreaType().name());
        dto.setDescription(mission.getDescription());
        dto.setCompleted(mission.isCompleted());
        dto.setDuration(mission.getDuration());
        dto.setSteps(mission.getSteps());
        return dto;
    }

    //Mission 엔티티 DTO 반환 - 버전 2
    private MissionResponse2DTO convertToDTO2(Mission mission) {
        MissionResponse2DTO dto = new MissionResponse2DTO();
        dto.setId(mission.getId());
        dto.setMissionName(mission.getMission_name());
        dto.setAreaName(mission.getArea().getAreaType().name());
        dto.setCompleted(mission.isCompleted());

        return dto;
    }


}
