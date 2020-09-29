package ch.heigvd.amt.mvcProject.infrastructure.persistence.memory;

import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implement in memory of the repository with a hashmap
 */
public class InMemoryQuestionRepository implements IQuestionRepository {

    private Map<QuestionId, Question> database = new ConcurrentHashMap<>();

    @Override
    public void save(Question question) {
        database.put(question.getId(), question);
    }

    @Override
    public void remove(QuestionId questionId) {
        database.remove(questionId);
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        Question existingQuestion = database.get(questionId);

        if (existingQuestion == null) {
            return Optional.empty();
        }

        return Optional.of(existingQuestion);
    }

    @Override
    public Collection<Question> findAll() {
        return database.values().stream().map(question -> Question.builder()
                .description(question.getDescription())
                .id(question.getId())
                .ranking(question.getRanking())
                .tags(question.getTags())
                .title(question.getTitle())
                .build()).collect(Collectors.toList());
    }


    @Override
    public boolean hasQuestion(QuestionId questionId) {
        return database.containsKey(questionId);
    }

    @Override
    public boolean hasQuestion(String title){

        for(Question question : database.values()){
            if(question.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}