package groom.Buddy_BE.character;

import groom.Buddy_BE.member.MemberInfoDTO;
import lombok.Data;

@Data
public class CharacterResponseDTO {
    private Long id;
    private String characterType;
    private String characterName;
    private int level;
    private MemberInfoDTO member;
}
