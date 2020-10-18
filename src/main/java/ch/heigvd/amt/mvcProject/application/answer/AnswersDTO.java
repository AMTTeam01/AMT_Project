package ch.heigvd.amt.mvcProject.application.answer;


import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class AnswersDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class AnswerDTO {
        private String description;
        private Date creationDate;
        private UserId userId;
    }

    @Singular
    private List<AnswerDTO> answers;
}
