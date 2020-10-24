package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.lenient;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class QuestionTest {

     @Mock
     User user;

     @BeforeEach
     private void prepareUser(){
         lenient().when(user.getUsername()).thenReturn("Jean");
         lenient().when(user.getId()).thenReturn(new UserId());
     }

     @Test
    public void deepCloneShouldReturnNewObject (){
        Question q1 = Question.builder()
                .title("titre")
                .description("description")
                .id(new QuestionId())
                .userId(user.getId())
                .username(user.getUsername())
                .creationDate(new Date())
                .build();

        Question q2 = q1.deepClone();

       assertEquals(q1, q2);
       assertFalse(q1 == q2);
        
    }

}
