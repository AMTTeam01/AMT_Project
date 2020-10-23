package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;


public class AnswerFacade {

    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Ask to the DB to insert a answer
     * @param command Answer command
     * @return the Answer DTO of the given command
     * @throws AnswerFailedException
     */
    public AnswersDTO.AnswerDTO addAnswer(AnswerCommand command) throws AnswerFailedException {

        try {
            Answer submittedAnswer = Answer.builder()
                    .description(command.getDescription())
                    .creationDate(command.getCreationDate())
                    .questionId(command.getQuestionId())
                    .userId(command.getUserId())
                    .username(command.getUsername())
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
     * @param id the id of the answer to delete
     */
    public void deleteAnswer(AnswerId id){
        answerRepository.remove(id);
    }
}
