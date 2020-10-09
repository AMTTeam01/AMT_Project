package ch.heigvd.amt.mvcProject.application.authentication.login;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@EqualsAndHashCode
public class CurrentUserDTO {
    private UserId userId;
    private String username;
    private String email;
}
