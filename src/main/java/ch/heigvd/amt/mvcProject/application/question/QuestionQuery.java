package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    QuestionId questionId;

    boolean withDetail;
}
