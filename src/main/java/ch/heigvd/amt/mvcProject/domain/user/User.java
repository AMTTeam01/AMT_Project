package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IEntity;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class User implements IEntity {

    @Setter(AccessLevel.NONE)
    private UserId id;

    private String username;

    private String email;

    private String password;

    public static class UserBuilder {

        public User build(){
            if(id == null){
                id = new UserId();
            }

            if(email == null){
                email = "";
            }

            return new User(id,username,email,password);
        }
    }

}
