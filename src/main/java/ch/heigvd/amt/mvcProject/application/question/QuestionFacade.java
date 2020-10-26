package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.answer.AnswerFailedException;
import ch.heigvd.amt.mvcProject.application.answer.AnswersDTO;
import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.application.comment.CommentFailedException;
import ch.heigvd.amt.mvcProject.application.comment.CommentQuery;
import ch.heigvd.amt.mvcProject.application.comment.CommentsDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
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

    private CommentFacade commentFacade;


    public QuestionFacade(IQuestionRepository questionRepository, UserFacade userFacade, CommentFacade commentFacade) {
        this.questionRepository = questionRepository;
        this.userFacade = userFacade;
        this.commentFacade = commentFacade;
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
                    .creationDate(submittedQuestion.getCreationDate())
                    .userId(submittedQuestion.getUserId())
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

        return getQuestionsAsDTO(allQuestions, new ArrayList<>(), new ArrayList<>());
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

        return getQuestionsAsDTO(questionsFound, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Return a single Question asked by query
     *
     * @param query Query passed
     * @return the single question asked
     * @throws QuestionFailedException
     */
    public QuestionsDTO.QuestionDTO getQuestion(QuestionQuery query)
            throws QuestionFailedException, CommentFailedException, AnswerFailedException {

        QuestionsDTO.QuestionDTO questionFound;
        Question question;
        Collection<AnswersDTO.AnswerDTO> answersDTO = new ArrayList<>();
        Collection<CommentsDTO.CommentDTO> commentsDTO = new ArrayList<>();


        if (query == null) {
            throw new QuestionFailedException("Query is null");
        } else {

            if (query.getQuestionId() != null) {
                if (!query.isWithDetail()) {
                    question = questionRepository.findById(query.getQuestionId())
                            .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));
                } else {
                    question = questionRepository.findByIdWithAllDetails(query.getQuestionId())
                            .orElseThrow(() -> new QuestionFailedException("The question hasn't been found"));

                    answersDTO = getAnswers(question);
                    commentsDTO = commentFacade.getComments(
                            CommentQuery.builder()
                                    .questionId(question.getId())
                                    .build()
                    ).getComments();

                }

                questionFound = getQuestionAsDTO(question, answersDTO, commentsDTO);


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
    private QuestionsDTO getQuestionsAsDTO(Collection<Question> questionsFound,
                                           Collection<AnswersDTO.AnswerDTO> answers,
                                           Collection<CommentsDTO.CommentDTO> comments) {
        List<QuestionsDTO.QuestionDTO> QuestionsDTOFound =
                questionsFound.stream().map(
                        question -> getQuestionAsDTO(question, answers, comments)).collect(Collectors.toList());

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
    private QuestionsDTO.QuestionDTO getQuestionAsDTO(Question
                                                              question, Collection<AnswersDTO.AnswerDTO> answers,
                                                      Collection<CommentsDTO.CommentDTO> comments) {

        return QuestionsDTO.QuestionDTO.builder()
                .title(question.getTitle())
                .description(question.getDescription())
                .id(question.getId())
                .userId(question.getUserId())
                .username(question.getUsername())
                .creationDate(question.getCreationDate())
                .answersDTO(AnswersDTO.builder().answers(answers).build())
                .commentsDTO(CommentsDTO.builder().comments(comments).build())
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
                answer -> {
                    Collection<CommentsDTO.CommentDTO> commentsDTO = new ArrayList<>();
                    try {
                        commentsDTO =
                                commentFacade.getComments(CommentQuery.builder().answerId(answer.getId()).build())
                                        .getComments();
                    } catch (CommentFailedException e) {
                        e.printStackTrace();
                    } catch (AnswerFailedException e) {
                        e.printStackTrace();
                    } catch (QuestionFailedException e) {
                        e.printStackTrace();
                    }


                    return AnswersDTO.AnswerDTO.builder()
                            .id(answer.getId())
                            .creationDate(answer.getCreationDate())
                            .description(answer.getDescription())
                            .username(answer.getUsername())
                            .comments(CommentsDTO.builder().comments(commentsDTO).build())
                            .build();
                }
        ).collect(Collectors.toList());
    }
}
