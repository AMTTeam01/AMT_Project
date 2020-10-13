package ch.heigvd.amt.mvcProject.application.user.edit;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class EditCommand {
    private String username;
    private String email;
    private String password;
    private String confirmationPassword;
}
