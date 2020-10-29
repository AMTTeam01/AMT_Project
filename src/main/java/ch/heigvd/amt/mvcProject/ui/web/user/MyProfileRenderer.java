package ch.heigvd.amt.mvcProject.ui.web.user;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.comment.CommentFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

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

    /**
     * Init servlet
     * @param config config for superclass
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questionFacade = serviceRegistry.getQuestionFacade();
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

        //TODO : Query to user specific questions
        QuestionsDTO questionsDTO = null;
        try {
            questionsDTO = questionFacade.getQuestions();
            request.setAttribute("questions", questionsDTO);
            request.getRequestDispatcher("/WEB-INF/views/myprofile.jsp").forward(request, response);
        } catch (UserFailedException | AnswerFailedException | QuestionFailedException | CommentFailedException e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
    }
}