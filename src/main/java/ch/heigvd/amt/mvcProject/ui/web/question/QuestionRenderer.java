package ch.heigvd.amt.mvcProject.ui.web.question;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionRenderer", urlPatterns = "/question")
public class QuestionRenderer extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //build a question command with attribute id in URL
        QuestionId id = (QuestionId) request.getAttribute("id");
        QuestionCommand command = QuestionCommand.builder().questionId(id).build();
        QuestionsDTO.QuestionDTO questionDTO = null;

        //check if question ID exists. If not, come back to browsing
        try {
            questionDTO = questionFacade.getCurrentQuestion(command);
            request.setAttribute("question", questionDTO);
            request.getRequestDispatcher("/WEB-INF/views/question.jsp").forward(request, response);
        } catch (QuestionFailedException e) {
            e.printStackTrace();
            response.sendRedirect("/browsing");
        }
    }
}
