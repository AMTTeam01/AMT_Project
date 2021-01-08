package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.ApiFailException;
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
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heigvd.amt.mvcProject.application.VoteUtils.getNewVoteValue;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    private UserFacade userFacade;

    private QuestionFacade questionFacade;

    private CommentFacade commentFacade;

    public AnswerFacade(IAnswerRepository answerRepository, UserFacade userFacade, QuestionFacade questionFacade,
                        CommentFacade commentFacade) {
        this.answerRepository = answerRepository;
        this.userFacade = userFacade;
        this.questionFacade = questionFacade;
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


        try {
            UsersDTO.UserDTO user = existingUser.getUsers().get(0);


            Answer submittedAnswer = Answer.builder()
                    .description(command.getDescription())
                    .creationDate(command.getCreationDate())
                    .questionId(existingQuestion.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .build();

            answerRepository.save(submittedAnswer);

            AnswersDTO.AnswerDTO newAnswer = AnswersDTO.AnswerDTO.builder()
                    .username(submittedAnswer.getUsername())
                    .description(submittedAnswer.getDescription())
                    .creationDate(submittedAnswer.getCreationDate())
                    .id(submittedAnswer.getId())
                    .build();

            // Send event to gamification API
            APIUtils.postCommentEvent(existingUser.getUsers().get(0).getId().asString());


            return newAnswer;
        } catch (ApiFailException e) {
            e.printStackTrace();
            throw new AnswerFailedException("Error with gamification server");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AnswerFailedException("Internal server error, retry later");
        } catch (Exception e) {
            throw new AnswerFailedException(e.getMessage());
        }
    }

    /**
     * Return AnswersDTO for the given query
     *
     * @param query query to filter result
     * @return AnswersDTO requested
     * @throws AnswerFailedException
     * @throws QuestionFailedException
     * @throws CommentFailedException
     */
    public AnswersDTO getAnswers(AnswerQuery query)
            throws AnswerFailedException, QuestionFailedException, CommentFailedException {

        Collection<Answer> answers;

        if (query == null) {
            throw new AnswerFailedException("Query is null");
        } else {

            if (query.getQuestionId() != null) {

                questionFacade.getQuestion(QuestionQuery.builder().questionId(query.getQuestionId()).build());

                answers = answerRepository.findByQuestionId(query.getQuestionId()).orElse(new ArrayList<>());

            } else {
                throw new AnswerFailedException("Query invalid");
            }
        }

        List<AnswersDTO.AnswerDTO> answersDTO = answers.stream().map(
                answer -> {
                    CommentsDTO commentsDTO = null;
                    try {
                        commentsDTO = commentFacade.getComments(CommentQuery.builder()
                                .answerId(answer.getId())
                                .build());
                    } catch (CommentFailedException e) {
                        e.printStackTrace();
                    } catch (AnswerFailedException e) {
                        e.printStackTrace();
                    } catch (QuestionFailedException e) {
                        e.printStackTrace();
                    }
                    return AnswersDTO.AnswerDTO.builder()
                            .username(answer.getUsername())
                            .description(answer.getDescription())
                            .creationDate(answer.getCreationDate())
                            .id(answer.getId())
                            .comments(commentsDTO)
                            .build();

                }
        ).collect(Collectors.toList());


        return AnswersDTO.builder().answers(answersDTO).build();
    }

    public AnswersDTO.AnswerDTO getAnswer(AnswerQuery query)
            throws AnswerFailedException, QuestionFailedException, CommentFailedException, UserFailedException {

        if (query == null)
            throw new AnswerFailedException("Query is null");

        if (query.getQuestionId() == null)
            throw new AnswerFailedException("Query invalid");

        // Check if the question exists
        questionFacade.getQuestion(QuestionQuery.builder().questionId(query.getQuestionId()).build());

        Answer answer = answerRepository.findById(query.getAnswerId())
                .orElseThrow(() -> new AnswerFailedException("The answer hasn't been found"));

        return getAnswerAsDTO(answer);
    }

    /**
     * Return the DTO of the answer in the parameter
     *
     * @param answer Answer to convert to DTO
     * @return the DTO corresponding to the parameter
     */
    private AnswersDTO.AnswerDTO getAnswerAsDTO(Answer answer) throws UserFailedException,
            CommentFailedException, AnswerFailedException, QuestionFailedException {
        // Author of the answer
        UsersDTO.UserDTO author = userFacade.getUsers(UserQuery.builder()
                .userId(answer.getUserId())
                .build()).getUsers().get(0);

        // Comments of the answer
        CommentsDTO comments = commentFacade.getComments(CommentQuery.builder()
                .answerId(answer.getId())
                .build());

        // Create answer to add it to the list of answers
        AnswersDTO.AnswerDTO build = AnswersDTO.AnswerDTO.builder()
                .username(author.getUsername())
                .description(answer.getDescription())
                .creationDate(answer.getCreationDate())
                .id(answer.getId())
                .comments(comments)
                .votes(answerRepository.getVotes(answer.getId()))
                .build();

        return build;
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

    /**
     * Vote on an answer
     *
     * @param userId   : id of the user voting
     * @param answerId : id of the answer being voted
     * @param vote     : value that is being done (upvote / downvote)
     */
    public void vote(UserId userId, AnswerId answerId, int vote) throws UserFailedException, AnswerFailedException {

        checkIfUserExists(userId);

        int voteValue = answerRepository.getVoteValue(userId, answerId);

        // Update the vote value
        voteValue = getNewVoteValue(voteValue, vote);

        answerRepository.addVote(userId, answerId, voteValue);
    }

    /**
     * Checks if the given user id is linked to an actual user
     *
     * @param userId : id of the user we want to search
     * @throws QuestionFailedException if the user doesn't exist
     * @throws UserFailedException
     */
    private void checkIfUserExists(UserId userId) throws AnswerFailedException, UserFailedException {
        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(userId).build());

        if (existingUser.getUsers().size() == 0)
            throw new AnswerFailedException("The user hasn't been found");
    }

}
