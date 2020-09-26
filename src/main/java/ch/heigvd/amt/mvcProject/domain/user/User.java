package ch.heigvd.amt.mvcProject.domain.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private String username;
    private String email;
}
