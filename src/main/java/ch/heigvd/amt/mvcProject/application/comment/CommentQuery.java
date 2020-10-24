package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class CommentQuery {

    private QuestionId questionId;

    private AnswerId answerId;


}
