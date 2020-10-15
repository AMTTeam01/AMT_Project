package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.answer.AnswersDTO;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import lombok.*;

import java.util.List;

/**
 * Object used to passe data between tier
 */
@Builder
@Getter
@EqualsAndHashCode
public class QuestionsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class QuestionDTO {
        private QuestionId id;
        private String title;
        private int ranking;
        private List<String> tags;
        private String description;
        private List<Answer> answers;
    }

    @Singular
    private List<QuestionDTO> questions;
}
