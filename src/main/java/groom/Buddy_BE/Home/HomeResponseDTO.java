package groom.Buddy_BE.Home;

import groom.Buddy_BE.character.CharacterResponseDTOForMission;
import groom.Buddy_BE.mission.MissionResponseDTO;
import lombok.Data;

@Data
public class HomeResponseDTO {
    //유저 닉네임
    private String member_nickname;

    //진행률
    private double  missionProPer;

    //캐릭터
    private CharacterResponseDTOForMission character;

    //미션명(제일 상단)
    private MissionResponseDTO mission;
}
