package ch.heigvd.amt.mvcProject.ui.web.user.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "NewQuestionRequestHandler", urlPatterns = "/new_question.do")
public class NewQuestionRequestHandler extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<String> tags_tmp = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3"));

        QuestionCommand command = QuestionCommand.builder()
                .title(req.getParameter("txt_title"))
                .description(req.getParameter("txt_description"))
                .tags(tags_tmp)
                .build();

        if (!serviceRegistry.hasQuestion(command.getTitle())) {
            resp.sendRedirect(getServletContext().getContextPath() + "/browsing");
        } else {
            resp.sendRedirect(getServletContext().getContextPath() + "/new_question");
        }
    }
}
