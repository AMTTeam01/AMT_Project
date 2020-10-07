package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
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
    public void getQuestionWhenEmptyReturnEmptyList() {
        assertTrue(questionFacade.getQuestions(null).getQuestions().size() == 0);
    }

    @Test
    @Order(2)
    public void addQuestionShouldWork() throws QuestionFailedException {
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


    @Test
    public void getQuestionByIdShouldWork() throws QuestionFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .ranking(1)
                .build();

        questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions(null);
        QuestionId id = view.getQuestions().get(0).getId();
        QuestionCommand commandID = QuestionCommand.builder()
                .questionId(id)
                .build();

        QuestionsDTO.QuestionDTO viewID = questionFacade.getQuestionsById(commandID);
        assertEquals(id, viewID.getId());
    }


}
