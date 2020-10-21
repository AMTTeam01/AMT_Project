package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
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

    public void deleteAnswer(AnswerId id){
        answerRepository.remove(id);
    }
}
