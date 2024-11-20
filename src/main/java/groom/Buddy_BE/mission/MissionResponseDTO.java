package groom.Buddy_BE.mission;

import lombok.Data;

import java.util.List;

@Data
public class MissionResponseDTO {
    private Long id; //미션 id
    private String missionName; // 미션 이름
    private String areaName;    // 영역 이름
    private String description; //미션 설명
    private String duration; //미션 기간
    private List<String> steps; //미션 단계

    private boolean isCompleted; // 완료 여부

}
