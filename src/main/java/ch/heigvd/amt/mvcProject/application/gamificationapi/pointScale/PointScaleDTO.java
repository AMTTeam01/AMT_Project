package ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale;

import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UserPointsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

public class PointScaleDTO {
    private List<UserPointsDTO> leaderboard;
    private String name;

    public PointScaleDTO() {
    }

    public PointScaleDTO(List<UserPointsDTO> leaderboard, String name) {
        this.leaderboard = leaderboard;
        this.name = name;
    }

    public List<UserPointsDTO> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<UserPointsDTO> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
