package groom.Buddy_BE.character;

import com.fasterxml.jackson.annotation.JsonBackReference;
import groom.Buddy_BE.member.Member;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    @JsonBackReference // 순환 참조 방지
    @ToString.Exclude // 순환 참조 방지
    private Member member;

    public enum CharacterType {
        CHICK,
        CAT,
        RABBIT,
        BEAR
    }
}
