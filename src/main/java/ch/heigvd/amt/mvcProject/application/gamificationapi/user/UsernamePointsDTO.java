package ch.heigvd.amt.mvcProject.application.gamificationapi.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UsernamePointsDTO {
    private String username;
    private int points;
}
