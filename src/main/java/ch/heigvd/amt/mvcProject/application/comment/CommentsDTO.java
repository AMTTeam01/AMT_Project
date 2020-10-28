package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.domain.comment.CommentId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class CommentsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class CommentDTO{
        private CommentId id;
        private String description;
        private Date creationDate;
        private String username;
    }

    @Singular
    private List<CommentDTO> comments;
}
