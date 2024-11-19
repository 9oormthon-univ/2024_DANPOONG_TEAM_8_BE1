package groom.Buddy_BE.member;

import lombok.Data;

@Data
public class MemberInfoDTO {
    private Long id;
    private Long kakaoId;
    private String nickname;
}
