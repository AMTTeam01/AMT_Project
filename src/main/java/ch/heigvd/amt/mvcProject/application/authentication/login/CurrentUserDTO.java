package ch.heigvd.amt.mvcProject.application.authentication.login;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrentUserDTO {
    private String username;
    private String email;
}
