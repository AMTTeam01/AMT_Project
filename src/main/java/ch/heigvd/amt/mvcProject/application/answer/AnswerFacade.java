package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerFacade {

    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void addAnswer(AnswerCommand command) {

        Answer submittedAnswer = Answer.builder()
                .description(command.getDescription())
                .creationDate(command.getCreationDate())
                .questionId(command.getQuestionId())
                .build();

        answerRepository.save(submittedAnswer);
    }

    public AnswersDTO getAnswersByQuestion(AnswerQuery query) throws AnswerFailedException {
        Collection<Answer> answers =
                answerRepository.findByQuestionId(query.getQuestionId())
                        .orElseThrow(() -> new AnswerFailedException("The answer hasn't been found"));

        List<AnswersDTO.AnswerDTO> answersDTO =
                answers.stream().map(
                        answer -> AnswersDTO.AnswerDTO.builder()
                                .description(answer.getDescription())
                                .id(answer.getId())
                                .build()).collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersDTO).build();
    }

}
