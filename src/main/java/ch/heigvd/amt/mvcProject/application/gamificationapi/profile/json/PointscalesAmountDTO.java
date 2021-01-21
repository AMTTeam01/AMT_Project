package ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class PointscalesAmountDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class PointscaleAmountDTO {
        private String name;
        private int amount;
    }
}
