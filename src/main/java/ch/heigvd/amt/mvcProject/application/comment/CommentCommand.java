package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
public class CommentCommand {

    private String description;
    private Date createDate;
    private QuestionId questionId;
    private UserId userId;
    private AnswerId answerId;
}
