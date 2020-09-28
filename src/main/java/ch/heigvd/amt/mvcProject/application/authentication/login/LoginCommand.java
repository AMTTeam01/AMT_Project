package ch.heigvd.amt.mvcProject.application.authentication.login;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class LoginCommand {
    private String username;
    private String clearTxtPassword;
}
