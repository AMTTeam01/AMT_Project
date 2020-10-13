package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@EqualsAndHashCode
public class UserDTO {
    private UserId userId;
    private String username;
    private String email;
}
