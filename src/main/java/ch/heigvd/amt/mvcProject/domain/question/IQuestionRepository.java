package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;

public interface IQuestionRepository extends IRepository<Question, QuestionId> {

    boolean hasQuestion(QuestionId questionId);

    boolean hasQuestion(String title);


}