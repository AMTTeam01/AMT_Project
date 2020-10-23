package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    private UserFacade userFacade;

    public AnswerFacade(IAnswerRepository answerRepository, UserFacade userFacade) {
        this.answerRepository = answerRepository;
        this.userFacade = userFacade;
    }

    /**
     * Ask to the DB to insert a answer
     *
     * @param command Answer command
     * @return the Answer DTO of the given command
     * @throws AnswerFailedException
     */
    public AnswersDTO.AnswerDTO addAnswer(AnswerCommand command) throws AnswerFailedException, UserFailedException {

        UsersDTO existingUser = userFacade.getUsers(UserQuery.builder().userId(command.getUserId()).build());

        if (existingUser.getUsers().size() == 0)
            new QuestionFailedException("The user hasn't been found");

        try {
            UsersDTO.UserDTO user = existingUser.getUsers().get(0);


            Answer submittedAnswer = Answer.builder()
                    .description(command.getDescription())
                    .creationDate(command.getCreationDate())
                    .questionId(command.getQuestionId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .build();

            answerRepository.save(submittedAnswer);

            AnswersDTO.AnswerDTO newAnswer = AnswersDTO.AnswerDTO.builder()
                    .username(submittedAnswer.getUsername())
                    .description(submittedAnswer.getDescription())
                    .creationDate(submittedAnswer.getCreationDate())
                    .build();

            return newAnswer;
        } catch (Exception e) {
            throw new AnswerFailedException(e.getMessage());
        }
    }

    /**
     * Ask to the DB to delete a answer
     *
     * @param id the id of the answer to delete
     */
    public void deleteAnswer(AnswerId id) {
        answerRepository.remove(id);
    }
}
