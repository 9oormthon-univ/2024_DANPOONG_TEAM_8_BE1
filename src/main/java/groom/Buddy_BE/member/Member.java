package groom.Buddy_BE.member;

import groom.Buddy_BE.area.Area;
import groom.Buddy_BE.character.Character;
import groom.Buddy_BE.checkList.CheckList;
import groom.Buddy_BE.mission.Mission;
import groom.Buddy_BE.missionRecord.MissionRecord;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    private String nickname;
    private String profileImageUrl;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Character character;

    @OneToMany(mappedBy = "member")
    private List<Area> areas = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CheckList> checkLists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionRecord> missionRecords = new ArrayList<>();
}
