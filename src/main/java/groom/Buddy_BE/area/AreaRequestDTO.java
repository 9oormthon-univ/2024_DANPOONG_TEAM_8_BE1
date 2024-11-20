package groom.Buddy_BE.area;

import lombok.Data;

@Data
public class AreaRequestDTO {
    private String areaType; // 클라이언트에서 enum 값을 문자열로 전달
}

