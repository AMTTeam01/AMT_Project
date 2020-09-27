package ch.heigvd.amt.mvcProject.application.question;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

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
        private String title;
    }

    @Singular
    private List<QuestionsDTO> questions;
}
