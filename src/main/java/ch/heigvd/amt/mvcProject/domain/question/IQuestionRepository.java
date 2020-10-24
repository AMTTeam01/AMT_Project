package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Optional;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    void upvote(UserId userId, QuestionId questionId);
    void downvote(UserId userId, QuestionId questionId);
    int getVotes(QuestionId questionId);

    /**
     * Retrieve a question with answers and comments
     * @param id question id
     * @return a question
     */
    Optional<Question> findByIdWithAllDetails(QuestionId id);

}