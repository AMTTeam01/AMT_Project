package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.Id;

import java.util.UUID;

public class CommentId extends Id {

    public CommentId() {
    }

    public CommentId(String id) {
        super(id);
    }

    public CommentId(UUID id) {
        super(id);
    }
}
