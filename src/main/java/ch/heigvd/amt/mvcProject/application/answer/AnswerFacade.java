package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UserQuery;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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

    public AnswersDTO getAnswers(AnswerQuery query) throws AnswerFailedException {

        Collection<Answer> answers = new ArrayList<>();
        if (query == null) {
            throw new AnswerFailedException("Query is null");
        } else {

            if (query.questionId != null) {

                answers= answerRepository.findByQuestionId(query.getQuestionId()).orElseThrow(
                        ()-> new AnswerFailedException("Answers for the given question not found")
                );
            } else {
                throw new AnswerFailedException("Query invalid");
            }
        }

        List<AnswersDTO.AnswerDTO> answersDTO = answers.stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                .username(answer.getUsername())
                .description(answer.getDescription())
                .creationDate(answer.getCreationDate())
                .id(answer.getId())
                .build()
        ).collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersDTO).build();
    }

    /**
     * Ask to the DB to delete a answer
     *
     * @param id the id of the answer to delete
     */
    public void removeAnswer(AnswerId id) {
        answerRepository.remove(id);
    }

}
