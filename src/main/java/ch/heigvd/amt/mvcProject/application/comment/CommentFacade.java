package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;

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

    public void addComment(CommentCommand command) throws UserFailedException {

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

}
