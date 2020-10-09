package ch.heigvd.amt.mvcProject.application.authentication.login;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@EqualsAndHashCode
public class CurrentUserDTO {
    private String username;
    private String email;
}
