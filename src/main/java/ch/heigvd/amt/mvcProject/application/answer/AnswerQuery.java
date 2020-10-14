package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AnswerQuery {
    private QuestionId questionId;
}
