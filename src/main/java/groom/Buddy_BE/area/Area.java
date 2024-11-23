package groom.Buddy_BE.area;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import groom.Buddy_BE.checkList.CheckList;
import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.mission.Mission;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "areas")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @OneToOne(mappedBy = "area", cascade = CascadeType.ALL)
    @JsonManagedReference
    private CheckList checkList;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Mission> missions = new ArrayList<>();


    public enum AreaType {
        DAILY_LIFE,
        SELF_MANAGEMENT,
        MONEY_MANAGEMENT,
        SOCIETY,
        ALL
    }

    //영역 완수 여부 필드
    @Column(nullable = false)
    private boolean isCompleted = false;
}



