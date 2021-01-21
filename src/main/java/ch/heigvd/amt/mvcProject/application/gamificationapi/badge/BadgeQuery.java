package ch.heigvd.amt.mvcProject.application.gamificationapi.badge;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class BadgeQuery {
    private UserId userId;
}
