package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ch.heigvd.amt.mvcProject.application.VoteUtils.DOWNVOTE;
import static ch.heigvd.amt.mvcProject.application.VoteUtils.UPVOTE;

@WebServlet(name = "AnswerVoteHandler", urlPatterns = "/a_vote")
public class AnswerVoteHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private AnswerFacade answerFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        answerFacade = serviceRegistry.getAnswerFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrentUserDTO currentUser = (CurrentUserDTO) req.getSession().getAttribute("currentUser");
        QuestionId questionId = new QuestionId(req.getParameter("question_id"));
        AnswerId answerId = new AnswerId(req.getParameter("answer_id"));

        String vote = req.getParameter("vote");

        try {
            if(vote.equals("upvote")) {
                answerFacade.vote(currentUser.getUserId(), answerId, UPVOTE);
            } else if (vote.equals("downvote")){
                answerFacade.vote(currentUser.getUserId(), answerId, DOWNVOTE);
            }
            resp.sendRedirect("/question?id=" + questionId.asString());
        } catch (AnswerFailedException | UserFailedException e) {
            e.printStackTrace();
        }
    }
}
