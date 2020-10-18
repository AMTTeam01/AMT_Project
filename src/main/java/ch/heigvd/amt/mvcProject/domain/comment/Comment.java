package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.IEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Comment implements IEntity<Comment, CommentId> {


    private CommentId id;
    private String description;
    private Date creationDate;

    @Override
    public Comment deepClone() {
        return this.toBuilder()
                .id(new CommentId())
                .build();
    }
    public static class CommentBuilder {

        public Comment builder() {

            if (id == null) {
                id = new CommentId();
            }

            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("description is mandatory");
            }

            if (creationDate == null) {
                creationDate = new Date();
            }

            return new Comment(id, description, creationDate);
        }
    }
}
