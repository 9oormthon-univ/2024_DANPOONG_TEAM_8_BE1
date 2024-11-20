package groom.Buddy_BE.missionRecord;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import groom.Buddy_BE.character.CharacterResponseDTOForMission;
import groom.Buddy_BE.member.MemberInfoDTO;
import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.Data;

@Data
public class MissionRecordResponseDTO {

    private Long id;
    private String content;
    private String feedback;

    //미션에 매핑되어있는 영역의 미션들이 모두 완수 되었는지에 대한 여부
    private boolean allCompleted;

    //미션 정보
    @JsonManagedReference
    private MissionResponseDTO missionResponseDTO;

    private MemberInfoDTO memberInfoDTO;

    private CharacterResponseDTOForMission characterResponseDTOForMission;
}
