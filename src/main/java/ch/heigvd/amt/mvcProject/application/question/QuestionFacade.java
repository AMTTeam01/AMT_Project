package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.answer.AnswersDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

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

    /**
     * Add a question to the repository
     * @param command : question with only relevant data
     * @return added question
     * @throws UserFailedException
     * @throws QuestionFailedException
     */
    public QuestionsDTO.QuestionDTO addQuestion(QuestionCommand command)
            throws UserFailedException, QuestionFailedException {

        checkIfUserExists(command.getUserId());

        try {
            // Create submitted question
            Question submittedQuestion = Question.builder()
                    .title(command.getTitle())
                    .description(command.getDescription())
                    .userId(command.getUserId())
                    .creationDate(command.getCreationDate())
                    .build();

            // Save to repository
            questionRepository.save(submittedQuestion);

            // Get author for the questionDTO
            UsersDTO.UserDTO user = userFacade.getUsers(
                    UserQuery.builder().userId(command.getUserId()).build()
            ).getUsers().get(0);

            // Get the answers for the questionDTO
            Collection<AnswersDTO.AnswerDTO> answersDTO = getAnswers(submittedQuestion);

            // create questionDTO of the submitted quesiton
            QuestionsDTO.QuestionDTO newQuestion = QuestionsDTO.QuestionDTO.builder()
                    .description(submittedQuestion.getDescription())
                    .id(submittedQuestion.getId())
                    .title(submittedQuestion.getTitle())
                    .username(user.getUsername())
                    .votes(0)
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
     * Upvotes a question, if already upvoted it will remove the upvote
     * @param userId : user that is upvoting
     * @param questionId : question being upvoted
     * @throws QuestionFailedException
     * @throws UserFailedException
     */
    public void upvote(UserId userId, QuestionId questionId) throws QuestionFailedException, UserFailedException {
        checkIfUserExists(userId);
        questionRepository.upvote(userId, questionId);
    }

    /**
     * Downvotes a question, if already downvoted it will remove the downvote
     * @param userId : user that is upvoting
     * @param questionId : question being upvoted
     * @throws QuestionFailedException
     * @throws UserFailedException
     */
    public void downvote(UserId userId, QuestionId questionId) throws QuestionFailedException, UserFailedException {
        checkIfUserExists(userId);
        questionRepository.downvote(userId, questionId);
    }

    /**
     * Retrieve all question in the repository
     * @return all questions as DTO
     */
    public QuestionsDTO getQuestions() throws UserFailedException {
        return getQuestionsDTO(questionRepository.findAll(), null);
    }

    /**
     * Retrieve questions asked by the query
     * @param query Query passed
     * @return return the result asked by the query as DTO
     * @throws QuestionFailedException
     */
    public QuestionsDTO getQuestions(QuestionQuery query) throws QuestionFailedException, UserFailedException {
        Collection<Question> questionsFound = new ArrayList<>();

        if (query == null)
            throw new QuestionFailedException("Query is null");

        if(query.getQuestionId() == null)
            throw new QuestionFailedException("Query invalid");

        Question question = questionRepository.findById(query.getQuestionId())
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));
        questionsFound.add(question);

        return getQuestionsDTO(questionsFound, query);
    }

    /**
     * Return a single Question asked by query
     * @param query Query passed
     * @return the single question asked
     * @throws QuestionFailedException
     */
    public QuestionsDTO.QuestionDTO getQuestion(QuestionQuery query) throws QuestionFailedException, UserFailedException {

        if (query == null || query.getQuestionId() == null)
            throw new QuestionFailedException("Query is null or invalid");

        Question question;

        if(query.isWithDetail()) {
            question = questionRepository.findByIdWithAllDetails(query.getQuestionId())
                    .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));
        } else {
            question = questionRepository.findById(query.getQuestionId())
                    .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));
        }

        return getQuestion(question, query);
    }

    /**
     * Transform a collection of Question into DTO
     *
     * @param questionsFound collection of questions
     * @return Questions DTO
     */
    private QuestionsDTO getQuestionsDTO(Collection<Question> questionsFound, QuestionQuery query) throws UserFailedException {
        List<QuestionsDTO.QuestionDTO> QuestionsDTOFound = new ArrayList<>();

        for (Question question : questionsFound) {
            QuestionsDTO.QuestionDTO questionDTO = getQuestion(question, query);
            QuestionsDTOFound.add(questionDTO);
        }

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
     * @return the DTO corresponding to the parameter
     */
    private QuestionsDTO.QuestionDTO getQuestion(Question question, QuestionQuery query) throws UserFailedException {

        // Get question's answers
        List<AnswersDTO.AnswerDTO> answersDTO = new ArrayList<>();
        if (query != null && query.isWithDetail()) {
            answersDTO = getAnswers(question);
        }

        // Get question user details
        UsersDTO.UserDTO user = userFacade.getUsers(
                UserQuery.builder().userId(question.getUserId()).build()
        ).getUsers().get(0);

        return QuestionsDTO.QuestionDTO.builder()
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .userId(question.getUserId())
                .username(user.getUsername())
                .creationDate(question.getCreationDate())
                .answersDTO(AnswersDTO.builder().answers(answersDTO).build())
                .build();
    }

    /**
     * Retrieve all answers associate to the question
     *
     * @param question Which question want the ansers
     * @return a collection of answer DTO associate to the question
     */
    private List<AnswersDTO.AnswerDTO> getAnswers(Question question) {
        return question.getAnswers().stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .id(answer.getId())
                        .creationDate(answer.getCreationDate())
                        .description(answer.getDescription())
                        .username(answer.getUsername())
                        .build()
        ).collect(Collectors.toList());
    }

    /**
     * Checks if the given user id is linked to an actual user
     * @param userId : id of the user we want to search
     * @throws QuestionFailedException
     * @throws UserFailedException
     */
    private void checkIfUserExists(UserId userId) throws QuestionFailedException, UserFailedException {
        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(userId).build());

        if (existingUser.getUsers().size() == 0)
            throw new QuestionFailedException("The user hasn't been found");

    }
}
