package ch.heigvd.amt.mvcProject.application.user.edit;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class EditUserCommand {
    private String id;
    private String username;
    private String email;
    private String password;
    private String confirmationPassword;
}
