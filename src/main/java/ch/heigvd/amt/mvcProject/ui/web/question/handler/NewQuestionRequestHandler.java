package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet(name = "NewQuestionRequestHandler", urlPatterns = "/new_question.do")
public class NewQuestionRequestHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO : gérer les tags

        List<String> tags_tmp = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3"));

        CurrentUserDTO currentUserDTO = (CurrentUserDTO) req.getSession().getAttribute("currentUser");

        QuestionCommand command = QuestionCommand.builder()
                .title(req.getParameter("txt_title"))
                .description(req.getParameter("txt_description"))
                .creationDate(new Date())
                .authorId(currentUserDTO.getUserId())
                .vote(0)
                .tags(tags_tmp)
                .build();

        try {
            questionFacade.addQuestion(command);
            resp.sendRedirect(getServletContext().getContextPath() + "/browsing");

        } catch (QuestionFailedException e) {
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
            resp.sendRedirect(getServletContext().getContextPath() + "/new_question");
        }
    }
}
