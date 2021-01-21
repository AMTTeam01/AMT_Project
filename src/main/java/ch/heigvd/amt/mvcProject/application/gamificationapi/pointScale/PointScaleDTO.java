package ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale;

import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UserPointsDTO;
import lombok.*;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class PointScaleDTO {
    private List<UserPointsDTO> leaderboard;
    private String name;
}
