package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestionDTOFacadeIT {

    private static ServiceRegistry serviceRegistry;
    private static QuestionFacade questionFacade;

    @BeforeAll
    public static void init(){
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Test
    public void publishQuestion(){
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("bla")
                .ranking(1)
                .build();

        questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions(null);
        assertNotNull(view);
        assertEquals(1, view.getQuestions().size());
        assertEquals(command.getTitle(), view.getQuestions().get(0).getTitle());

    }
}
