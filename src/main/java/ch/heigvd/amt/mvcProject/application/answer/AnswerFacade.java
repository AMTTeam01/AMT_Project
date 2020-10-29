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
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class AnswerFacade {

    // Vote values
    private static final int UPVOTE   =  1;
    private static final int NOVOTE   =  0;
    private static final int DOWNVOTE = -1;

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

        if (query == null)
            throw new AnswerFailedException("Query is null");

        if (query.getQuestionId() == null)
            throw new AnswerFailedException("Query invalid");

        // Check if the question exists (else throw error)
        questionFacade.getQuestion(QuestionQuery.builder().questionId(query.getQuestionId()).build());

        Collection<Answer> answers = answerRepository.findByQuestionId(query.getQuestionId())
                .orElse(new ArrayList<>());

        List<AnswersDTO.AnswerDTO> answersDTO = new ArrayList<>();
        for (Answer answer : answers) {
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


    /**
     * Upvotes an answer, if already upvoted it will remove the upvote
     * @param userId : user that is upvoting
     * @param answerId : answer being upvoted
     * @throws QuestionFailedException
     * @throws UserFailedException
     */
    public void upvote(UserId userId, AnswerId answerId) throws QuestionFailedException, UserFailedException {
        checkIfUserExists(userId);
        vote(userId, answerId, UPVOTE);
    }

    /**
     * Downvotes an answer, if already downvoted it will remove the downvote
     * @param userId : user that is upvoting
     * @param answerId : answer being upvoted
     * @throws QuestionFailedException
     * @throws UserFailedException
     */
    public void downvote(UserId userId, AnswerId answerId) throws QuestionFailedException, UserFailedException {
        checkIfUserExists(userId);
        vote(userId, answerId, DOWNVOTE);
    }


    /**
     * Vote on a question
     * @param userId : id of the user voting
     * @param answerId : id of the answer being voted
     * @param vote : value that is being done (upvote / downvote)
     */
    private void vote(UserId userId, AnswerId answerId, int vote) {
        int voteValue = 0;

        // Check if the user already voted
        if (answerRepository.hasAlreadyVoted(userId, answerId)) {
            voteValue = answerRepository.getVoteValue(userId, answerId);
        }

        // Update the vote value
        voteValue = getNewVoteValue(voteValue, vote);

        answerRepository.addVote(userId, answerId, voteValue);
    }

    /**
     * TODO : refactor
     * Get the vote value when voting on a quesiton
     * @param startVoteValue : start vote value of the user (if he already voted on the question)
     * @param voteValue : vote value of the current vote
     * @return the new vote value
     */
    private int getNewVoteValue(int startVoteValue, int voteValue) {
        int result = 0;

        if(voteValue == UPVOTE) {
            switch(startVoteValue) {
                case NOVOTE:
                case DOWNVOTE:
                    result = UPVOTE;
                    break;
                case UPVOTE:
                    result = 0;
                    break;
            }
        } else if (voteValue == DOWNVOTE) {
            switch(startVoteValue) {
                case NOVOTE:
                case UPVOTE:
                    result = DOWNVOTE;
                    break;
                case DOWNVOTE:
                    result = 0;
                    break;
            }
        }

        return result;
    }

    /**
     * Checks if the given user id is linked to an actual user
     * @param userId : id of the user we want to search
     * @throws QuestionFailedException if the user doesn't exist
     * @throws UserFailedException
     */
    private void checkIfUserExists(UserId userId) throws QuestionFailedException, UserFailedException {
        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(userId).build());

        if (existingUser.getUsers().size() == 0)
            throw new QuestionFailedException("The user hasn't been found");
    }


}
