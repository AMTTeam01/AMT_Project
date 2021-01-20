package ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class BadgesAwardDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class BadgeAwardDTO{
        private String reason;
        private String timestamp;
        private String path;
    }

    @Singular
    private List<BadgesAwardDTO.BadgeAwardDTO> badgeAwardDTOS;

}
