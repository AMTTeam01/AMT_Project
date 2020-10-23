package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
// Submit
public class AnswerCommand {

    private String description;
    private Date creationDate;
    private QuestionId questionId;
    private UserId userId;
    private String username;
}
