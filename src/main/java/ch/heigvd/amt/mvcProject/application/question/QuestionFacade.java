package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;

/**
 * Link the question and the domain, what we offer to the user to interact with the domain
 * In this class we pass a command (to modify data) of a query (to get data)
 */
public class QuestionFacade {

    private IQuestionRepository questionRepository;

    public QuestionFacade(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Add a new question in memory
     * @param command Object that contains question information
     */
    public void addNewUser(QuestionCommand command) {
        Question submittedQuestion = Question.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .tags(command.getTags())
                .build();

        questionRepository.save(submittedQuestion);
    }




}
