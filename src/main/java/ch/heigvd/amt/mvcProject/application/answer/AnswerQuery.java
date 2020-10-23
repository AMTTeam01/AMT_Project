package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode

// Request
public class AnswerQuery {

    QuestionId questionId;
}
