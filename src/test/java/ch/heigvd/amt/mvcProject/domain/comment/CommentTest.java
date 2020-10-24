package ch.heigvd.amt.mvcProject.domain.comment;

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
class CommentTest {
    @Mock
    Question question;

    @Mock
    User user;

    @BeforeEach
    private void prepare() {
        lenient().when(question.getId()).thenReturn(new QuestionId());
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

}