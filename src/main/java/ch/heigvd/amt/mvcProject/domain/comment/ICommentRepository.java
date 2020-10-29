package ch.heigvd.amt.mvcProject.domain.comment;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.ArrayList;
import java.util.Optional;

public interface ICommentRepository extends IRepository<Comment, CommentId> {

    /**
     * Return the list of comment associate with the question id
     * @param questionId The id of the question
     * @return Return Optional.empty if the question id is not in the repo, otherwise the list of comments
     */
    Optional<ArrayList<Comment>> findByQuestionId(QuestionId questionId);

    /**
     * Return the list of comment associate with the answer id
     * @param answerId The id of the answer
     * @return Return Optional.empty if the question id is not in the repo, otherwise the list of comments
     */
    Optional<ArrayList<Comment>> findByAnswerId(AnswerId answerId);

}
