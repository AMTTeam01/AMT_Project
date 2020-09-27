package ch.heigvd.amt.mvcProject.presentation.handler;

import ch.heigvd.amt.mvcProject.business.QuestionService;
import ch.heigvd.amt.mvcProject.business.UserService;
import ch.heigvd.amt.mvcProject.model.Question;
import ch.heigvd.amt.mvcProject.model.User;
import ch.heigvd.amt.mvcProject.model.UserRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "NewQuestionRequestHandler", urlPatterns = "/request.new_question")
public class NewQuestionRequestHandler extends HttpServlet {

    private QuestionService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = QuestionService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Question question = Question.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .build();

        // tmp : new User()
        service.storeQuestion(null, question);

        resp.sendRedirect(getServletContext().getContextPath());
    }
}
