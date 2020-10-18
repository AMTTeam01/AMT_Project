package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;

import java.util.Optional;

public interface ICommentRepository extends IRepository<Comment, CommentId> {

    Optional<User> findByQuestionId(QuestionId questionId);

    Optional<User> findByAnswerId(AnswerId answerId);

}
