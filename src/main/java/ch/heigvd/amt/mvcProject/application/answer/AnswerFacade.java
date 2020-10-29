package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.application.comment.CommentFailedException;
import ch.heigvd.amt.mvcProject.application.comment.CommentQuery;
import ch.heigvd.amt.mvcProject.application.comment.CommentsDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    private UserFacade userFacade;
    private QuestionFacade questionFacade;
    private CommentFacade commentFacade;

    public AnswerFacade() {
    }

    public AnswerFacade(IAnswerRepository answerRepository, UserFacade userFacade, QuestionFacade questionFacade) {
        this.answerRepository = answerRepository;
        this.userFacade = userFacade;
        this.questionFacade = questionFacade;
    }

    public void setAnswerRepository(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setQuestionFacade(QuestionFacade questionFacade) {
        this.questionFacade = questionFacade;
    }

    public void setCommentFacade(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    /**
     * Ask to the DB to insert a answer
     *
     * @param command Answer command
     * @return the Answer DTO of the given command
     * @throws AnswerFailedException
     */
    public AnswersDTO.AnswerDTO addAnswer(AnswerCommand command)
            throws UserFailedException, QuestionFailedException, AnswerFailedException, CommentFailedException {

        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(command.getUserId()).build());

        if (existingUser.getUsers().size() == 0)
            throw new UserFailedException("The user hasn't been found");

        QuestionsDTO.QuestionDTO existingQuestion = questionFacade.getQuestion(QuestionQuery.builder()
                .questionId(command.getQuestionId())
                .build());

        UsersDTO.UserDTO user = existingUser.getUsers().get(0);

        Answer submittedAnswer = Answer.builder()
                .description(command.getDescription())
                .creationDate(command.getCreationDate())
                .questionId(existingQuestion.getId())
                .userId(user.getId())
                .build();

        answerRepository.save(submittedAnswer);

        AnswersDTO.AnswerDTO newAnswer = AnswersDTO.AnswerDTO.builder()
                .username(user.getUsername())
                .description(submittedAnswer.getDescription())
                .creationDate(submittedAnswer.getCreationDate())
                .id(submittedAnswer.getId())
                .build();

        return newAnswer;
    }

    /**
     * Return AnswersDTO for the given query
     * @param query query to filter result
     * @return AnswersDTO requested
     * @throws AnswerFailedException
     * @throws QuestionFailedException
     * @throws CommentFailedException
     */
    public AnswersDTO getAnswers(AnswerQuery query)
            throws AnswerFailedException, QuestionFailedException,
            CommentFailedException, UserFailedException {

        Collection<Answer> answers;

        if (query == null) {
            throw new AnswerFailedException("Query is null");
        } else {

            if (query.getQuestionId() != null) {
                questionFacade.getQuestion(QuestionQuery.builder().questionId(query.getQuestionId()).build());
                answers = answerRepository.findByQuestionId(query.getQuestionId())
                        .orElse(new ArrayList<>());
            } else {
                throw new AnswerFailedException("Query invalid");
            }
        }

        List<AnswersDTO.AnswerDTO> answersDTO = new ArrayList<>();
        for (Answer answer : answers) {
            AnswersDTO.AnswerDTO build = AnswersDTO.AnswerDTO.builder()
                    .username(userFacade.getUsers(
                            UserQuery.builder()
                                    .userId(answer.getUserId())
                                    .build()
                    ).getUsers().get(0).getUsername())
                    .description(answer.getDescription())
                    .creationDate(answer.getCreationDate())
                    .id(answer.getId())
                    .comments(commentFacade.getComments(
                            CommentQuery.builder()
                            .answerId(answer.getId())
                            .build()))
                    .build();
            answersDTO.add(build);
        }

        return AnswersDTO.builder().answers(answersDTO).build();
    }

    /**
     * Ask to the DB to delete a answer
     *
     * @param id the id of the answer to delete
     */
    public void removeAnswer(AnswerId id) throws AnswerFailedException {
        answerRepository.findById(id).orElseThrow(
                () -> new AnswerFailedException("Answer not found")
        );


        answerRepository.remove(id);
    }

}
