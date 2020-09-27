package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * Encapsulate question information
 */
@Builder
@Getter
@EqualsAndHashCode
public class QuestionCommand {

    private QuestionId questionId;

    private String title;

    private String description;

    private List<String> tags;


}
