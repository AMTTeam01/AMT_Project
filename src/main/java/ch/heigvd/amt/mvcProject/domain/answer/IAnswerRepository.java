package ch.heigvd.amt.mvcProject.domain.answer;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.ArrayList;
import java.util.Optional;

public interface IAnswerRepository extends IRepository<Answer, AnswerId> {

    /**
     * Return all the annswers for the given question id
     * @param questionId the question id
     * @return List of answers
     */
    Optional<ArrayList<Answer>> findByQuestionId(QuestionId questionId);
}
