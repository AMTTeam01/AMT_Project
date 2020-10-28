package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CommentTest {
    @Mock
    Question question;

    @Mock
    Answer answer;

    @Mock
    User user;

    @BeforeEach
    private void prepare() {
        lenient().when(question.getId()).thenReturn(new QuestionId());
        lenient().when(answer.getId()).thenReturn(new AnswerId());
        lenient().when(user.getUsername()).thenReturn("Jean");
        lenient().when(user.getId()).thenReturn(new UserId());
    }

    @Test
    public void deepClone_ShouldReturnNewObject_WhenCalled() {
        Comment c1 = Comment.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .questionId(question.getId())
                .description("Bla")
                .creationDate(new Date())
                .build();

        Comment c2 = c1.deepClone();

        assertEquals(c1, c2);
        assertFalse(c1 == c2);
    }

    @Test
    public void builder_ShouldThrowError_IfUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Comment.builder()
                        .username(user.getUsername())
                        .questionId(question.getId())
                        .description("Bla")
                        .creationDate(new Date())
                        .build()
        );
    }

    @Test
    public void builder_ShouldThrowError_IfQuestionIdAndAnswerAreBothNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Comment.builder()
                        .username(user.getUsername())
                        .userId(user.getId())
                        .description("Bla")
                        .creationDate(new Date())
                        .build()
        );
    }

    @Test
    public void builder_ShouldThrowError_IfQuestionIdAndAnswerIdAreBothPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> Comment.builder()
                        .username(user.getUsername())
                        .userId(user.getId())
                        .questionId(question.getId())
                        .answerId(answer.getId())
                        .description("Bla")
                        .creationDate(new Date())
                        .build()
        );
    }
}