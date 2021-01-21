package ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale;

import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UserPointsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Builder
@Getter
@EqualsAndHashCode
public class PointScaleDTO {
    private List<UserPointsDTO> leaderboard;
    private String name;
}
