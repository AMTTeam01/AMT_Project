package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Optional;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    /**
     * Add a vote to a question
     * @param userId : id of the user voting the question
     * @param questionId : id of the question being voted
     * @param positive : value being voted (-1, 0, 1)
     */
    void addVote(UserId userId, QuestionId questionId, int positive);

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

    /**
     * Check if a user already voted a specified question
     * @param userId : id of the user
     * @param questionId : id of the question
     * @return true if the user already voted on the question
     */
    boolean hasAlreadyVoted(UserId userId, QuestionId questionId);

    /**
     * Get the vote value of a question from a specified user
     * @param userId : user
     * @param questionId : question
     * @return vote value that the user made (-1, 0, 1)
     */
    int getVoteValue(UserId userId, QuestionId questionId);
}