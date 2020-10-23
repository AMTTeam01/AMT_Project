package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.answer.AnswersDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Link the question and the domain, what we offer to the user to interact with the domain
 * In this class we pass a command (to modify data) of a query (to get data)
 */
public class QuestionFacade {

    private IQuestionRepository questionRepository;

    private UserFacade userFacade;


    public QuestionFacade(IQuestionRepository questionRepository, UserFacade userFacade) {
        this.questionRepository = questionRepository;
        this.userFacade = userFacade;
    }

    public QuestionsDTO.QuestionDTO addQuestion(QuestionCommand command)
            throws UserFailedException, QuestionFailedException {
        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(command.getUserId()).build());

        if (existingUser.getUsers().size() == 0)
            new QuestionFailedException("The user hasn't been found");

        try {

            UsersDTO.UserDTO user = existingUser.getUsers().get(0);

            Question submittedQuestion = Question.builder()
                    .title(command.getTitle())
                    .description(command.getDescription())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .creationDate(command.getCreationDate())
                    .build();

            questionRepository.save(submittedQuestion);

            Collection<AnswersDTO.AnswerDTO> answersDTO = getAnswers(submittedQuestion);

            QuestionsDTO.QuestionDTO newQuestion = QuestionsDTO.QuestionDTO.builder()
                    .description(submittedQuestion.getDescription())
                    .id(submittedQuestion.getId())
                    .title(submittedQuestion.getTitle())
                    .username(submittedQuestion.getUsername())
                    .creationDate(submittedQuestion.getCreationDate())
                    .userId(submittedQuestion.getUserId())
                    .answersDTO(AnswersDTO.builder().answers(answersDTO).build())
                    .build();

            return newQuestion;

        } catch (Exception e) {
            throw new QuestionFailedException(e.getMessage());
        }
    }

    /**
     * Retrieve all question in the repo
     *
     * @return all questions as DTO
     */
    public QuestionsDTO getQuestions() {
        Collection<Question> allQuestions = questionRepository.findAll();

        return getQuestionsDTO(allQuestions, null);
    }

    /**
     * Retrieve questions asked by the query
     *
     * @param query Query passed
     * @return return the result asked by the query as DTO
     * @throws QuestionFailedException
     */
    public QuestionsDTO getQuestions(QuestionQuery query) throws QuestionFailedException {
        Collection<Question> questionsFound = new ArrayList<>();

        if (query == null) {
            throw new QuestionFailedException("Query is null");
        } else {

            if (query.getQuestionId() != null) {
                Question question = questionRepository.findById(query.getQuestionId())
                        .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

                questionsFound.add(question);

            } else {
                throw new QuestionFailedException("Query invalid");

            }
        }

        return getQuestionsDTO(questionsFound, null);
    }

    /**
     * Return a single Question asked by query
     *
     * @param query Query passed
     * @return the single question asked
     * @throws QuestionFailedException
     */
    public QuestionsDTO.QuestionDTO getQuestion(QuestionQuery query) throws QuestionFailedException {

        QuestionsDTO.QuestionDTO questionFound;

        if (query == null) {
            throw new QuestionFailedException("Query is null");
        } else {

            if (query.getQuestionId() != null) {
                Question question = questionRepository.findById(query.getQuestionId())
                        .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

                Collection<AnswersDTO.AnswerDTO> answersDTO = new ArrayList<>();
                if (query.isWithDetail()) {
                    answersDTO = getAnswers(question);
                }

                questionFound = getQuestion(question, answersDTO);


            } else {
                throw new QuestionFailedException("Query invalid");

            }
        }

        return questionFound;
    }

    /**
     * Transform a collection of Question into DTO
     *
     * @param questionsFound collection of questions
     * @return Questions DTO
     */
    private QuestionsDTO getQuestionsDTO(Collection<Question> questionsFound,
                                         Collection<AnswersDTO.AnswerDTO> answers) {
        List<QuestionsDTO.QuestionDTO> QuestionsDTOFound =
                questionsFound.stream().map(
                        question -> getQuestion(question, answers)).collect(Collectors.toList());

        return QuestionsDTO.builder().questions(QuestionsDTOFound).build();
    }

    /**
     * Remove a question
     *
     * @param id the id of the question to be removed
     * @throws QuestionFailedException
     */
    public void removeQuestion(QuestionId id) throws QuestionFailedException {
        questionRepository.findById(id)
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

        questionRepository.remove(id);
    }

    /**
     * Return the DTO of the question in the parameter
     *
     * @param question question to transform
     * @param answers  list of answer to the question
     * @return the DTO corresponding to the parameter
     */
    private QuestionsDTO.QuestionDTO getQuestion(Question question, Collection<AnswersDTO.AnswerDTO> answers) {
        return QuestionsDTO.QuestionDTO.builder()
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .userId(question.getUserId())
                .username(question.getUsername())
                .creationDate(question.getCreationDate())
                .answersDTO(AnswersDTO.builder().answers(answers).build())
                .build();
    }

    /**
     * Retrieve all answers associate to the question
     *
     * @param question Which question want the ansers
     * @return a collection of answer DTO associate to the question
     */
    private Collection<AnswersDTO.AnswerDTO> getAnswers(Question question) {
        return question.getAnswers().stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .id(answer.getId())
                        .creationDate(answer.getCreationDate())
                        .description(answer.getDescription())
                        .username(answer.getUsername())
                        .build()
        ).collect(Collectors.toList());
    }
}
