package ch.heigvd.amt.mvcProject.domain.question;

import ch.heigvd.amt.mvcProject.domain.IRepository;


public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    void addVote(int voteValue, QuestionId id);
}