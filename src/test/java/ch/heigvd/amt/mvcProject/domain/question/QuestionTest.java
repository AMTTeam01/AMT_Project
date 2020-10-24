package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
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
     User userJean;

     @Mock
     User userMarie;

     @BeforeEach
     private void prepareUser(){
         lenient().when(userJean.getUsername()).thenReturn("Jean");
         lenient().when(userJean.getId()).thenReturn(new UserId());

         lenient().when(userMarie.getUsername()).thenReturn("Marie");
         lenient().when(userMarie.getId()).thenReturn(new UserId());
     }

     @Test
    public void deepCloneShouldReturnNewObject (){
        Question q1 = Question.builder()
                .title("titre")
                .description("description")
                .userId(userJean.getId())
                .username(userJean.getUsername())
                .creationDate(new Date())
                .build();

        Question q2 = q1.deepClone();

       assertEquals(q1, q2);
       assertFalse(q1 == q2);
        
    }

    @Test
    public void addAnswer_ShouldAddAAnswerToTheQuestion_WhenCalled(){
         Question question = Question.builder()
                 .title("titre")
                 .description("description")
                 .userId(userJean.getId())
                 .username(userJean.getUsername())
                 .creationDate(new Date())
                 .build();

        Answer answer = Answer.builder()
                .userId(userMarie.getId())
                .creationDate(new Date())
                .description("Answer")
                .questionId(question.getId())
                .username(userMarie.getUsername())
                .build();

        question.addAnswer(answer);

        assertEquals(1, question.getAnswers().size());
    }

}
