package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;


public class CommentFacade {

    private ICommentRepository commentRepository;

    public CommentFacade(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(CommentCommand command) {

        Comment submittedComment = Comment.builder()
                .description(command.getDescription())
                .creationDate(command.getCreateDate())
                .build();

        commentRepository.save(submittedComment);
    }


}
