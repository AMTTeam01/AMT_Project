package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.ArrayList;
import java.util.Optional;

public interface ICommentRepository extends IRepository<Comment, CommentId> {

    Optional<ArrayList<Comment>> findByQuestionId(QuestionId questionId);

    Optional<ArrayList<Comment>> findByAnswerId(AnswerId answerId);

}
