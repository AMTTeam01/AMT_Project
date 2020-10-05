package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.EqualsAndHashCode;

public class UserCommand {

    private UserId id;
    private String username;
    private String email;

    @EqualsAndHashCode.Exclude
    private String encryptedPassword;
}
