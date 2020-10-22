package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.*;

import java.util.Date;
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
        private String username;
        private UserId userid;
        private List<String> tags;
        private String description;
        private Date creationDate;
    }

    @Singular
    private List<QuestionDTO> questions;
}
