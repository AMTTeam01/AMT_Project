package ch.heigvd.amt.mvcProject.ui.web.question;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.question.*;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionRenderer", urlPatterns = "/question")
public class QuestionRenderer extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //build a question command with attribute id in URL
        QuestionId id = new QuestionId(request.getParameter("id"));
        QuestionQuery query = QuestionQuery.builder().questionId(id).build();
        QuestionsDTO.QuestionDTO questionDTO = null;

        //check if question ID exists. If not, come back to browsing
        try {
            questionDTO = questionFacade.getQuestionById(query);
            request.setAttribute("question", questionDTO);
            request.getRequestDispatcher("/WEB-INF/views/question.jsp").forward(request, response);
        } catch (QuestionFailedException e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
    }
}
