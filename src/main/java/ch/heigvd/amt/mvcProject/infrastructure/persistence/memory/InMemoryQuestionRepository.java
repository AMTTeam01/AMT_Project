package ch.heigvd.amt.mvcProject.infrastructure.persistence.memory;

import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implement in memory of the repository with a hashmap
 */
@ApplicationScoped
@Named("InMemoryQuestionRepository")
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements IQuestionRepository {
}