package groom.Buddy_BE.mission;

import lombok.Data;

@Data
public class MissionResponseDTO {
    private String missionName; // 미션 이름
    private String areaName;    // 영역 이름
    private boolean isCompleted; // 완료 여부
}
