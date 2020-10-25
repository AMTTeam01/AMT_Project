package ch.heigvd.amt.mvcProject.ui.web.question;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BrowsingRenderer", urlPatterns = "/browsing")
public class BrowsingRenderer extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        QuestionsDTO questionsDTO = null;
        String search = request.getParameter("txt_search");

        if(search == null || search.isEmpty()) {
            questionsDTO = questionFacade.getAllQuestions();
        } else {
            QuestionQuery query = QuestionQuery.builder().title(search).build();
            try {
                questionsDTO = questionFacade.getQuestions(query);
            } catch (QuestionFailedException e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("questions", questionsDTO);
        request.getRequestDispatcher("/WEB-INF/views/browsing.jsp").forward(request, response);
    }
}
