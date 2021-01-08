package ch.heigvd.amt.mvcProject.application.gamificationapi.badge;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class BadgesDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class BadgeDTO{
        private String name;
        private String imageUrl;
    }

    @Singular
    private List<BadgesDTO.BadgeDTO> badges;

}
