package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.*;

import java.util.List;

/**
 * Object used to passe data between tier
 */
@Builder
@Getter
@EqualsAndHashCode
public class UsersDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class UserDTO {
        private UserId id;
        private String username;
        private String email;
    }

    @Singular
    private List<UserDTO> users;
}
