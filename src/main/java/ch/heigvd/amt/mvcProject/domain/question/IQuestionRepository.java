package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    void addVote(UserId userId, QuestionId questionId);
}