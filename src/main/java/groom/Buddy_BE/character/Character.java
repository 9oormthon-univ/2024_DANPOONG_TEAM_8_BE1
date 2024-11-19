package groom.Buddy_BE.character;

import groom.Buddy_BE.member.Member;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CharacterType characterType;

    private String characterName;

    private int level = 1;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public enum CharacterType {
        CHICK,
        CAT,
        RABBIT,
        BEAR
    }
}
