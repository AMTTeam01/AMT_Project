package ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale;

import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UsernamePointsDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class PointScaleUsernameDTO {
    private List<UsernamePointsDTO> leaderboard;
    private String name;
}
