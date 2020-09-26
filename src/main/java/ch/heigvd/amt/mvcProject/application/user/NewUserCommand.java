package ch.heigvd.amt.mvcProject.application.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class NewUserCommand {

    private String username;

    private String email;

    private String password;


}
