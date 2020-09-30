package ch.heigvd.amt.mvcProject.ui.web.question;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "BrowsingRenderer", urlPatterns = "/browsing")
public class BrowsingRenderer extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade = serviceRegistry.getQuestionFacade();

        //Create fake questions
        // TODO : Need to me remove
        questionFacade.addQuestion(QuestionCommand.builder().
                title("test")
                .ranking(1)
                .description("Description")
                .tags(Arrays.asList("tag1", "tag2"))
                .build());
        questionFacade.addQuestion(QuestionCommand.builder()
                .title("test2")
                .ranking(3)
                .description("Description")
                .tags(Arrays.asList("tag1", "tag2"))
                .build());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        QuestionsDTO questionsDTO = questionFacade.getQuestions(null);
        request.setAttribute("questions", questionsDTO);
        request.getRequestDispatcher("/WEB-INF/views/browsing.jsp").forward(request, response);
    }
}
