package groom.Buddy_BE.checkList;

import groom.Buddy_BE.area.AreaInfoDTO;
import groom.Buddy_BE.member.MemberInfoDTO;
import lombok.Data;

@Data
public class CheckListResponseDTO {
    private Long id;
    private int fistQ;
    private int secondQ;
    private int thirdQ;
    private int fourthQ;
    private int fifthQ;
    private int sixthQ;
    private int seventhQ;
    private int eighthQ;
    private AreaInfoDTO area;
    private MemberInfoDTO member;
}
