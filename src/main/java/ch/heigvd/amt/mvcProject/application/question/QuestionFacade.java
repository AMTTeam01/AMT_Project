package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Link the question and the domain, what we offer to the user to interact with the domain
 * In this class we pass a command (to modify data) of a query (to get data)
 */
public class QuestionFacade {

    private IQuestionRepository questionRepository;

    public QuestionFacade(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void addQuestion(QuestionCommand command) throws QuestionFailedException {

        try {

            Question submittedQuestion = Question.builder()
                    .title(command.getTitle())
                    .description(command.getDescription())
                    .ranking(command.getRanking())
                    .tags(command.getTags())
                    .build();

            questionRepository.save(submittedQuestion);
        }catch(Exception e){
            throw new QuestionFailedException(e.getMessage());
        }
    }

    public QuestionsDTO getQuestions(QuestionQuery query){
        Collection<Question> allQuestions = questionRepository.findAll();

        List<QuestionsDTO.QuestionDTO> allQuestionsDTO =
                allQuestions.stream().map(
                        question -> QuestionsDTO.QuestionDTO.builder()
                                .title(question.getTitle())
                                .ranking(question.getRanking())
                                .tags(question.getTags())
                                .description(question.getDescription())
                                .id(question.getId())
                                .build()).collect(Collectors.toList());

        return QuestionsDTO.builder().questions(allQuestionsDTO).build();
    }

    public QuestionsDTO.QuestionDTO getQuestionById(QuestionQuery query) throws QuestionFailedException {
        Question question = questionRepository.findById(query.getQuestionId())
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

        QuestionsDTO.QuestionDTO currentQuestionDTO = QuestionsDTO.QuestionDTO.builder()
                .ranking(question.getRanking())
                .tags(question.getTags())
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .build();

        return currentQuestionDTO;
    }

}
