package groom.Buddy_BE.Home;

import groom.Buddy_BE.member.MemberInfoDTO;
import lombok.Data;

@Data
public class MypageResponseDTO {
    private MemberInfoDTO member;
    private String email;
}
