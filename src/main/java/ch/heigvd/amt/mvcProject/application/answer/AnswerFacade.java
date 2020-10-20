package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerFacade {

    private IAnswerRepository answerRepository;
    private IUserRepository userRepository;

    public AnswerFacade(IAnswerRepository answerRepository, IUserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public void addAnswer(AnswerCommand command) throws AnswerFailedException {

        try {
            Answer submittedAnswer = Answer.builder()
                    .description(command.getDescription())
                    .creationDate(command.getCreationDate())
                    .questionId(command.getQuestionId())
                    .username(command.getUsername())
                    .build();

            answerRepository.save(submittedAnswer);
        } catch (Exception e) {
            throw new AnswerFailedException(e.getMessage());
        }
    }

        public AnswersDTO getAnswersByQuestion(AnswerQuery query) throws AnswerFailedException {
        Collection<Answer> answers =
                answerRepository.findByQuestionId(query.getQuestionId())
                        .orElseThrow(() -> new AnswerFailedException("The answer hasn't been found"));

        List<AnswersDTO.AnswerDTO> answersDTO =
                answers.stream().map(
                        answer -> AnswersDTO.AnswerDTO.builder()
                                .description(answer.getDescription())
                                .creationDate(answer.getCreationDate())
                                .username(answer.getUsername())
                                .build()).collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersDTO).build();
    }

}
