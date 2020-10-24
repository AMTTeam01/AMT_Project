package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
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

            QuestionsDTO.QuestionDTO newQuestion = QuestionsDTO.QuestionDTO.builder()
                    .description(submittedQuestion.getDescription())
                    .id(submittedQuestion.getId())
                    .title(submittedQuestion.getTitle())
                    .username(submittedQuestion.getUsername())
                    .votes(0)
                    .creationDate(submittedQuestion.getCreationDate())
                    .userId(submittedQuestion.getUserId())
                    .build();

            return newQuestion;

        } catch (Exception e) {
            throw new QuestionFailedException(e.getMessage());
        }
    }

    public void upvote(UserId userId, QuestionId questionId) throws QuestionFailedException {
        try {
            questionRepository.upvote(userId, questionId);
        } catch(Exception e){
            throw new QuestionFailedException(e.getMessage());
        }
    }

    public void downvote(UserId userId, QuestionId questionId) throws QuestionFailedException {
        try {
            questionRepository.downvote(userId, questionId);
        } catch(Exception e){
            throw new QuestionFailedException(e.getMessage());
        }
    }

    /**
     * Retrieve all question in the repo
     * @return all questions as DTO
     */
    public QuestionsDTO getQuestions() {
        Collection<Question> allQuestions = questionRepository.findAll();

        return getQuestionsDTO(allQuestions);
    }

    /**
     * Retrieve questions asked by the query
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

        return getQuestionsDTO(questionsFound);
    }

    /**
     * Return a single Question asked by query
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

                questionFound = QuestionsDTO.QuestionDTO.builder()
                        .title(question.getTitle())
                        .description(question.getDescription())
                        .votes(questionRepository.getVotes(question.getId()))
                        .id(question.getId())
                        .build();
            } else {
                throw new QuestionFailedException("Query invalid");

            }
        }

        return questionFound;
    }

    /**
     * Transform a collection of Question into DTO
     * @param questionsFound collection of questions
     * @return Questions DTO
     */
    private QuestionsDTO getQuestionsDTO(Collection<Question> questionsFound) {
        List<QuestionsDTO.QuestionDTO> QuestionsDTOFound =
                questionsFound.stream().map(
                        question -> QuestionsDTO.QuestionDTO.builder()
                                .title(question.getTitle())
                                .description(question.getDescription())
                                .id(question.getId())
                                .votes(questionRepository.getVotes(question.getId()))
                                .userId(question.getUserId())
                                .username(question.getUsername())
                                .creationDate(question.getCreationDate())
                                .build()).collect(Collectors.toList());

        return QuestionsDTO.builder().questions(QuestionsDTOFound).build();
    }

    /**
     * Remove a question
     * @param id the id of the question to be removed
     * @throws QuestionFailedException
     */
    public void removeQuestion(QuestionId id) throws QuestionFailedException {
        questionRepository.findById(id)
                .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

        questionRepository.remove(id);
    }
}
