package ch.heigvd.amt.mvcProject.application.gamificationapi.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

public class UserPointsDTO {
    private String username;
    private int points;

    public UserPointsDTO() {
    }

    public UserPointsDTO(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
