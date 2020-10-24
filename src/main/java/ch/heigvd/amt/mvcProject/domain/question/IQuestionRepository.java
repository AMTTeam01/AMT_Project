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
}