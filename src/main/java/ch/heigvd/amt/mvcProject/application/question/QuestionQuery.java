package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    QuestionId questionId;
    UserId userId;
}
