package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserTest {


    @Test
    public void deepCloneShouldReturnNewObject (){
        User u1 = User.builder()
                .email("henri@gmail.com")
                .username("henri")
                .clearTextPassword("test")
                .build();

        User u2 = u1.deepClone();

        assertEquals(u1, u2);
        assertNotSame(u1, u2);

    }

    @Test
    public void clearTextPasswordShouldEncryptPassword() {
        String password = "test";

        User u1 = User.builder()
                .email("henri@gmail.com")
                .username("henri")
                .clearTextPassword(password)
                .build();

        assertTrue(u1.authenticate(password));
    }

    @Test
    public void authenticateShouldFailIfPasswordIsIncorrect(){

        String password = "test";

        User u1 = User.builder()
                .email("henri@gmail.com")
                .username("henri")
                .clearTextPassword(password)
                .build();

        assertFalse(u1.authenticate("notTheSamePassword"));
    }
}
