package groom.Buddy_BE.mission;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import groom.Buddy_BE.area.Area;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.missionRecord.MissionRecord;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mission_name;

    @Column(nullable = false)
    public boolean isCompleted = false;

    private String description;

    private String duration;

    @ElementCollection
    private List<String> steps;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference // 순환 참조 방지
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    @JsonBackReference // 순환 참조 방지
    private Area area;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private MissionRecord missionRecord;
}
