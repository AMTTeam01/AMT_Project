package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Optional;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    /**
     * Upvotes a question (+1)
     * @param userId : id of the voter
     * @param questionId : id of the question
     */
    void upvote(UserId userId, QuestionId questionId);

    /**
     * Downvotes a question (-1)
     * @param userId : id of the voter
     * @param questionId : id of the question
     */
    void downvote(UserId userId, QuestionId questionId);

    /**
     * Get the total votes of a question
     * @param questionId : id of the question
     * @return total votes for that question
     */
    int getVotes(QuestionId questionId);

    /**
     * Retrieve a question with answers and comments
     * @param id question id
     * @return a question
     */
    Optional<Question> findByIdWithAllDetails(QuestionId id);

}