package ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class BadgesAmountDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class BadgeAmountDTO {
        private String name;
        private int amount;
    }

    @Singular
    private List<BadgeAmountDTO> badges;

}
