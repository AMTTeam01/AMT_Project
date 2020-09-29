package ch.heigvd.amt.mvcProject.application.authentication.register;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class RegisterCommand {
    private String username;
    private String email;
    private String clearTxtPassword;
    private String confirmationClearTxtPassword;
}
