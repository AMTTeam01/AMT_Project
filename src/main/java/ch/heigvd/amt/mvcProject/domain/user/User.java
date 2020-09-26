package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IEntity;
import lombok.*;

@Data
@Builder
public class User implements IEntity {

    @Setter(AccessLevel.NONE)
    private UserId id = new UserId();

    private String username;

    private String email;

    private String password;
}
