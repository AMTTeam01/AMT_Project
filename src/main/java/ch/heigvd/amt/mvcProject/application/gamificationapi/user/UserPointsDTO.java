package ch.heigvd.amt.mvcProject.application.gamificationapi.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class UserPointsDTO {
    private String username;
    private int points;
}
