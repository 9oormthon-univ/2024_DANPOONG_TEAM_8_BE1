package groom.Buddy_BE.character;

import lombok.Data;

@Data
public class CharacterResponseDTOForMission {
    private Long id;
    private String characterType;
    private String characterName;
    private int level;
}
