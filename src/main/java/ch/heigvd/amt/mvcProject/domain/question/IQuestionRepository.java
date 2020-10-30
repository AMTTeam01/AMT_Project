package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Optional;
import java.util.Collection;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    /**
     * Retrieve a question with answers and comments
     * @param id question id
     * @return a question
     */
    Optional<Question> findByIdWithAllDetails(QuestionId id);
    Collection<Question> findByUserId(UserId userId);
    Collection<Question> findByTitleContaining(String title);

    /**
     * Get the value of a user's vote on a question
     * @param userId : user
     * @param questionId : question
     * @return the vote value, VoteUtils.NONEXISTANT if it doesn't exist
     */
    int getVoteValue(UserId userId, QuestionId questionId);

    /**
     * Adds a vote to a question
     * @param userId     : user that voted
     * @param questionId : question to add the vote to
     * @param vote       : vote value (-1, 0 or 1)
     */
    void addVote(UserId userId, QuestionId questionId, int vote);

    /**
     * Get the total number of votes of a question
     * @param questionId : question
     * @return total number of votes
     */
    int getVotes(QuestionId questionId);
}