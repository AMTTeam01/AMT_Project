package ch.heigvd.amt.mvcProject.application.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

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
    public static class UserDTO{
        private String username;
    }

    @Singular
    private List<UsersDTO> users;
}
