package ch.heigvd.amt.mvcProject.domain.answer;

import ch.heigvd.amt.mvcProject.domain.IEntity;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Answer implements IEntity<Answer, AnswerId> {

    @Setter(AccessLevel.NONE)
    private AnswerId id;

    private String description;

    private Date creationDate;

    private QuestionId questionId;

    private UserId userId;

    private String username;

    private List<Comment> comments;


    @Override
    public Answer deepClone() {
        return this.toBuilder()
                .id(new AnswerId(id.asString()))
                .build();
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public static class AnswerBuilder {
        public Answer build() {
            if (id == null) {
                id = new AnswerId();
            }

            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("description is mandatory");
            }

            if (creationDate == null) {
                throw new IllegalArgumentException("CreationDate is mandatory");
            }

            if (questionId == null) {
                throw new IllegalArgumentException("questionId is mandatory");
            }

            if (userId == null) {
                throw new IllegalArgumentException("userId is mandatory");
            }

            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("username is mandatory");
            }

            if(comments == null){
                comments = new ArrayList<>();
            }

            return new Answer(id, description, creationDate, questionId, username, comments);
        }
    }
}
