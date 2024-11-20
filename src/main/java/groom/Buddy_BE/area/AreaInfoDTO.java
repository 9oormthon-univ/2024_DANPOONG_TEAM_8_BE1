package groom.Buddy_BE.area;

import lombok.Data;

@Data
public class AreaInfoDTO {
    private Long id;
    private String areaType;

    public AreaInfoDTO(Long id, String areaType) {
        this.id = id;
        this.areaType = areaType;
    }
}
