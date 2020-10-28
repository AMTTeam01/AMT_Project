package ch.heigvd.amt.mvcProject.application.answer;

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
import ch.heigvd.amt.mvcProject.domain.question.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    private UserFacade userFacade;

    private QuestionFacade questionFacade;

    public AnswerFacade(IAnswerRepository answerRepository, UserFacade userFacade, QuestionFacade questionFacade) {
        this.answerRepository = answerRepository;
        this.userFacade = userFacade;
        this.questionFacade = questionFacade;
    }

    /**
     * Ask to the DB to insert a answer
     *
     * @param command Answer command
     * @return the Answer DTO of the given command
     * @throws AnswerFailedException
     */
    public AnswersDTO.AnswerDTO addAnswer(AnswerCommand command)
            throws UserFailedException, QuestionFailedException, AnswerFailedException {

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

            return newAnswer;
        } catch (Exception e) {
            throw new AnswerFailedException(e.getMessage());
        }
    }

    public AnswersDTO getAnswers(AnswerQuery query) throws AnswerFailedException, QuestionFailedException, UserFailedException {

        Collection<Answer> answers = new ArrayList<>();
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
    public void removeAnswer(AnswerId id) throws AnswerFailedException {
        answerRepository.findById(id).orElseThrow(
                () -> new AnswerFailedException("Answer not found")
        );


        answerRepository.remove(id);
    }

}
