package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;

import java.util.Optional;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    /**
     * Retreive a question with answers and comments
     * @param id question id
     * @return a question
     */
    Optional<Question> findByIdWithAllDetails(QuestionId id);
}