package ch.heigvd.amt.mvcProject.domain.answer;


import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class AnswerTest {

    @Mock
    Question question;

    @Mock
    User user;

    @BeforeEach
    private void prepare(){
        lenient().when(question.getId()).thenReturn(new QuestionId());
        lenient().when(user.getUsername()).thenReturn("Jean");
        lenient().when(user.getId()).thenReturn(new UserId());
    }

    @Test
    public void deepClone_ShouldReturnNewObject_WhenCalled(){
        Answer a1 = Answer.builder()
                .creationDate(new Date())
                .description("Test")
                .questionId(question.getId())
                .username(user.getUsername())
                .userId(user.getId())
                .build();

        Answer a2 = a1.deepClone();

        assertEquals(a1,a2);
        assertFalse(a1 == a2);
    }

    @Test
    public void addComment_ShouldAddCommentInTheCommentsArray_WhenCalled(){
        Answer a = Answer.builder()
                .creationDate(new Date())
                .description("Test")
                .questionId(question.getId())
                .username(user.getUsername())
                .userId(user.getId())
                .build();

        Comment c = Comment.builder()
                .creationDate(new Date())
                .answerId(a.getId())
                .description("Comments")
                .username(user.getUsername())
                .userId(user.getId())
                .build();

        a.addComment(c);

        assertEquals(1, a.getComments().size());
    }

}
