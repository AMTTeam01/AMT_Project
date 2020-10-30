package ch.heigvd.amt.mvcProject.domain.answer;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.ArrayList;
import java.util.Optional;

public interface IAnswerRepository extends IRepository<Answer, AnswerId> {

    /**
     * Return all the annswers for the given question id
     * @param questionId the question id
     * @return List of answers
     */
    Optional<ArrayList<Answer>> findByQuestionId(QuestionId questionId);

    /**
     * Get the value of a user's vote on an answer
     * @param userId : user
     * @param answerId : answer
     * @return the vote value, VoteUtils.NONEXISTANT if it doesn't exist
     */
    int getVoteValue(UserId userId, AnswerId answerId);

    /**
     * Adds a vote to an answer
     * @param userId   : user that voted
     * @param answerId : answer to add the vote to
     * @param vote     : vote value (-1, 0 or 1)
     */
    void addVote(UserId userId, AnswerId answerId, int vote);

    /**
     * Get the total number of votes of a question
     * @param answerId : question
     * @return total number of votes
     */
    int getVotes(AnswerId answerId);
}
