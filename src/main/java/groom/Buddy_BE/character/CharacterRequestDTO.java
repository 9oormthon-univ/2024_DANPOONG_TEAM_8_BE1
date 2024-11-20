package groom.Buddy_BE.character;

import lombok.Data;

@Data
public class CharacterRequestDTO {
    private String characterType; // 캐릭터 타입 (Enum 값: CHICK, CAT, RABBIT, BEAR)
    private String characterName; // 캐릭터 이름
}