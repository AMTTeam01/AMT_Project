package ch.heigvd.amt.mvcProject.application.gamificationapi.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class UserPointsDTO {
    private String userId;
    private int totalPoints;
}
