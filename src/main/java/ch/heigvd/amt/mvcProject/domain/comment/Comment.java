package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.IEntity;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Comment implements IEntity<Comment, CommentId> {

    @Setter(AccessLevel.NONE)
    private CommentId id;

    private String description;

    private Date creationDate;

    private QuestionId questionId;

    private AnswerId answerId;

    private UserId userId;

    private String username;

    @Override
    public Comment deepClone() {
        return this.toBuilder()
                .id(new CommentId())
                .build();
    }

    public static class CommentBuilder {

        public Comment build() {

            if (id == null) {
                id = new CommentId();
            }

            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("description is mandatory");
            }

            if (creationDate == null) {
                throw new IllegalArgumentException("CreationDate is mandatory");
            }

            if (questionId == null && answerId == null) {
                throw new IllegalArgumentException("questionId or answerId is mandatory");
            }

            if (userId == null) {
                throw new IllegalArgumentException("UserId or answerId is mandatory");
            }
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username is mandatory");
            }


            return new Comment(id, description, creationDate, questionId, answerId, userId,username);
        }
    }
}
