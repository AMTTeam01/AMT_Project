package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerCommand;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AnswerHandler", urlPatterns = "/answer.do")
public class AnswerHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private AnswerFacade answerFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        answerFacade = serviceRegistry.getAnswerFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().removeAttribute("errors");

        AnswerCommand answerCommand = AnswerCommand.builder()
                .creationDate(new Date()) // TODO add time
                .description(req.getParameter("txt_answer"))
                .questionId(new QuestionId(req.getParameter("hidden_id")))
                .build();


        try {
            answerFacade.addAnswer(answerCommand);
            resp.sendRedirect( getServletContext().getContextPath() + "/question?id=" + req.getParameter("hidden_id"));
        } catch (AnswerFailedException e) {
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
        }

    }
}
