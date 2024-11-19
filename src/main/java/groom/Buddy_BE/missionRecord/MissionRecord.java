package groom.Buddy_BE.missionRecord;

import com.fasterxml.jackson.annotation.JsonBackReference;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.mission.Mission;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "mission_records")
public class MissionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    @JsonBackReference // 순환 참조 방지
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference // 순환 참조 방지
    private Member member;
}
