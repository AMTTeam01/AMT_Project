package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

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

@WebServlet(name = "QuestionVoteHandler", urlPatterns = "/vote")
public class QuestionVoteHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // We differentiate a downvote or an upvote from the upvote boolean
        // TODO : modifier la db pour ajouter une valeur (upvote / downvote)
        QuestionId questionId = new QuestionId(req.getParameter("id"));
        UserId userId = new UserId(req.getParameter("currentUser"));
        boolean upvote = req.getParameter("vote").equals("upvote");

        try {
            if(upvote) {
                questionFacade.upvote(userId, questionId);
            } else {
                questionFacade.downvote(userId, questionId);
            }
            resp.sendRedirect("/question?id=" + questionId.asString());
        } catch (QuestionFailedException e) {
            e.printStackTrace();
        }
    }
}
