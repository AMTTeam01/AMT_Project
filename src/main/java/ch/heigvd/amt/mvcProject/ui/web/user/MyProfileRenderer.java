package ch.heigvd.amt.mvcProject.ui.web.user;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyProfileServlet", urlPatterns = "/my_profile")
public class MyProfileRenderer extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;
    private BadgeFacade badgeFacade;

    /**
     * Init servlet
     * @param config config for superclass
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questionFacade = serviceRegistry.getQuestionFacade();
        badgeFacade = serviceRegistry.getBadgeFacade();
    }

    /**
     * GET renderer, will retrieve questions from the current user
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object errors = request.getSession().getAttribute("errors");
        request.setAttribute("errors", errors);
        request.getSession().removeAttribute("errors");

        CurrentUserDTO currentUser = (CurrentUserDTO) request.getSession().getAttribute("currentUser");
        request.setAttribute("user", currentUser);
        
        QuestionQuery query = QuestionQuery.builder().userId(currentUser.getUserId()).build();
        BadgeQuery badgeQuery = BadgeQuery.builder().userId(currentUser.getUserId()).build();

        QuestionsDTO questionsDTO = null;
        try {
            questionsDTO = questionFacade.getQuestions(query);
        } catch (QuestionFailedException e) {
            e.printStackTrace();
        }

        try {
            badgeFacade.getBadges(badgeQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("questions", questionsDTO);

        request.getRequestDispatcher("/WEB-INF/views/myprofile.jsp").forward(request, response);
    }
}