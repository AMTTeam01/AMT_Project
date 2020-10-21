package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.answer.AnswersDTO;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Link the question and the domain, what we offer to the user to interact with the domain
 * In this class we pass a command (to modify data) of a query (to get data)
 */
public class QuestionFacade {

    private IQuestionRepository questionRepository;
    private IUserRepository userRepository;

    public QuestionFacade(IQuestionRepository questionRepository, IUserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

        public QuestionsDTO.QuestionDTO addQuestion(QuestionCommand command) throws QuestionFailedException {
        try {
            Question submittedQuestion = Question.builder()
                    .title(command.getTitle())
                    .description(command.getDescription())
                    .username(command.getUsername())
                    .creationDate(command.getCreationDate())
                    .build();

            questionRepository.save(submittedQuestion);

            QuestionsDTO.QuestionDTO newQuestion = QuestionsDTO.QuestionDTO.builder()
                    .description(submittedQuestion.getDescription())
                    .id(submittedQuestion.getId())
                    .title(submittedQuestion.getTitle())
                    .username(submittedQuestion.getUsername())
                    .build();

            return newQuestion;
        } catch (Exception e) {
            throw new QuestionFailedException(e.getMessage());
        }
    }

    public QuestionsDTO getQuestions(QuestionQuery query) {
        Collection<Question> allQuestions = questionRepository.findAll();

        List<QuestionsDTO.QuestionDTO> allQuestionsDTO =
                allQuestions.stream().map(
                        question -> QuestionsDTO.QuestionDTO.builder()
                                .title(question.getTitle())
                                .ranking(question.getVote())
                                .description(question.getDescription())
                                .id(question.getId())
                                .build()).collect(Collectors.toList());

        return QuestionsDTO.builder().questions(allQuestionsDTO).build();
    }

    public QuestionsDTO.QuestionDTO getQuestionById(QuestionQuery query) throws QuestionFailedException {
        Question question = questionRepository.findById(query.getQuestionId())
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));


        QuestionsDTO.QuestionDTO currentQuestionDTO = QuestionsDTO.QuestionDTO.builder()
                .ranking(question.getVote())
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .build();

        return currentQuestionDTO;
    }


    public QuestionsDTO.QuestionDTO getQuestionByIdWithDetails(QuestionQuery query) throws QuestionFailedException {
        Question question = questionRepository.findByIdWithAllDetails(query.getQuestionId())
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

        List<AnswersDTO.AnswerDTO> answersDTO = question.getAnswers().stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .creationDate(answer.getCreationDate())
                        .description(answer.getDescription())
                        .username(answer.getUsername())
                        .build()
        ).collect(Collectors.toList());

        QuestionsDTO.QuestionDTO currentQuestionDTO = QuestionsDTO.QuestionDTO.builder()
                .ranking(question.getVote())
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .answers(answersDTO)
                .build();

        return currentQuestionDTO;
    }

}
