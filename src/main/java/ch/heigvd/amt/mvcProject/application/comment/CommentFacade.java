package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.CommentId;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class CommentFacade {

    private ICommentRepository commentRepository;

    private UserFacade userFacade;

    public CommentFacade(ICommentRepository commentRepository,
                         UserFacade userFacade) {
        this.commentRepository = commentRepository;
        this.userFacade = userFacade;
    }

    public CommentsDTO.CommentDTO addComment(CommentCommand command) throws UserFailedException,
            CommentFailedException {

        if(command.getAnswerId() != null && command.getQuestionId() != null){
            throw new CommentFailedException("Command invalid, answerId and questionId can't be mentioned at the " +
                    "same time");
        }

        UsersDTO existingUser = userFacade.getUsers(
                UserQuery.builder()
                        .userId(command.getUserId())
                        .build()
        );

        if (existingUser.getUsers().size() == 0)
            throw new UserFailedException("The user hasn't been found");


        Comment submittedComment = Comment.builder()
                .description(command.getDescription())
                .creationDate(command.getCreateDate())
                .userId(command.getUserId())
                .username(existingUser.getUsers().get(0).getUsername())
                .answerId(command.getAnswerId())
                .questionId(command.getQuestionId())
                .build();

        commentRepository.save(submittedComment);

        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .id(submittedComment.getId())
                .username(submittedComment.getUsername())
                .description(submittedComment.getDescription())
                .creationDate(submittedComment.getCreationDate())
                .build();

        return commentDTO;
    }

    public CommentsDTO getComments() {
        Collection<Comment> allComments = commentRepository.findAll();

        return getCommentsAsDTO(allComments);
    }

    public CommentsDTO getComments(CommentQuery query) throws CommentFailedException {
        Collection<Comment> commentsFound = new ArrayList<>();

        if (query == null) {
            throw new CommentFailedException("Query is null");
        } else {
            if (query.getQuestionId() != null && query.getAnswerId() == null) {
                commentsFound = commentRepository.findByQuestionId(query.getQuestionId())
                        .orElseThrow(() -> new CommentFailedException(
                                "The comments associate with the id question " + query.getQuestionId()
                                        .asString() + " hasn't be " +
                                        "found"));

            } else if (query.getAnswerId() != null) {
                commentsFound = commentRepository.findByAnswerId(query.getAnswerId())
                        .orElseThrow(() -> new CommentFailedException(
                                "The comments associate with the id answer " + query.getAnswerId()
                                        .asString() + " hasn't be found"));
            } else {
                throw new CommentFailedException("Query invalid");
            }
        }

        return getCommentsAsDTO(commentsFound);
    }

    private CommentsDTO getCommentsAsDTO(Collection<Comment> comments) {
        List<CommentsDTO.CommentDTO> commentDTOList = comments.stream().map(
                comment -> getCommentAsDTO(comment)
        ).collect(Collectors.toList());

        return CommentsDTO.builder().comments(commentDTOList).build();
    }

    private CommentsDTO.CommentDTO getCommentAsDTO(Comment comment) {
        return CommentsDTO.CommentDTO.builder()
                .creationDate(comment.getCreationDate())
                .description(comment.getDescription())
                .username(comment.getUsername())
                .build();
    }

    public void removeComment(CommentId id) throws CommentFailedException {
        commentRepository.findById(id).orElseThrow(
                () -> new CommentFailedException("Comment not found")
        );

        commentRepository.remove(id);
    }

}
