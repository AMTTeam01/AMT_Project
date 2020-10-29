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
     * Check if a user already voted a specified answer
     * @param userId : id of the user
     * @param answerId : id of the answer
     * @return true if the user already voted on the answer
     */
    boolean hasAlreadyVoted(UserId userId, AnswerId answerId);

    /**
     * Get the vote value of an answer from a specified user
     * @param userId : user
     * @param answerId : answer
     * @return vote value that the user made (-1, 0, 1)
     */
    int getVoteValue(UserId userId, AnswerId answerId);


    /**
     * Add a vote to an answer
     * @param userId : id of the user voting the answer
     * @param answerId : id of the answer being voted
     * @param positive : value being voted (-1, 0, 1)
     */
    void addVote(UserId userId, AnswerId answerId, int positive);

    /**
     * Get the total votes of an answer
     * @param answerId : id of the answer
     * @return total votes for that answer
     */
    int getVotes(AnswerId answerId);
}
