package ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json;

import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgesDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class BadgesAwardWithBadgeDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class BadgeAwardWithBadgeDTO{
        private String reason;
        private Timestamp timestamp;
        private BadgesDTO.BadgeDTO badgeDTO;
    }

    @Singular
    private List<BadgesAwardDTO.BadgeAwardDTO> badgeAwardDTOS;

}
