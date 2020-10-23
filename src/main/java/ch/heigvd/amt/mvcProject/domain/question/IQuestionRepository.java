package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Collection;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    Collection<Question> findByUserId(UserId userId);
    Collection<Question> findByTitleContaining(String title);
}