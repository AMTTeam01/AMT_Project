package ch.heigvd.amt.mvcProject.application.gamificationapi.profile;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class PointsAwardsDTO {
    public static class PointAwardDTO {
        private String reason;
        private String timestamp;
        private String path;
        private int amount;
    }
}
