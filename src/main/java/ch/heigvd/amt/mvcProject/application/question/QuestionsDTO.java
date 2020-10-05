package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.domain.question.Question;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Object used to pass data between tier
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
        private int ranking;
        private List<String> tags;
    }

    @Singular
    private List<QuestionDTO> questions;
}
