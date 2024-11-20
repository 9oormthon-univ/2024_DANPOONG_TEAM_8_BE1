package groom.Buddy_BE.mission;

import lombok.Data;

import java.util.List;

@Data
public class MissionResponse2DTO {
    private Long id; //미션 id
    private String missionName; // 미션 이름
    private String areaName;    // 영역 이름

    private boolean isCompleted; // 완료 여부

}
