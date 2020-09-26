package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class UserCommand {

    private UserId userId;

    private String username;

    private String email;

    private String password;


}
