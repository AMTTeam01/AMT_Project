package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionFacadeTest {

    private static ServiceRegistry serviceRegistry;
    private static QuestionFacade questionFacade;

    @BeforeAll
    public static void init() {
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Test
    @Order(1)
    public void getQuestion_WhenEmpty_ReturnEmptyList() {
        assertTrue(questionFacade.getQuestions(null).getQuestions().size() == 0);
    }

    @Test
    public void addQuestion() throws QuestionFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .ranking(1)
                .build();

        questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions(null);
        assertNotNull(view);
        assertEquals(1, view.getQuestions().size());
        assertEquals(command.getTitle(), view.getQuestions().get(0).getTitle());
    }


}
