package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.comment.CommentCommand;
import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "CommentHandler", urlPatterns = "/comment_question.do")
public class CommentHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;

    private CommentFacade commentFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        commentFacade = serviceRegistry.getCommentFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.getSession().removeAttribute("errors");

        // retrieve the username
        CurrentUserDTO currentUserDTO = (CurrentUserDTO) req.getSession().getAttribute("currentUser");

        // Question id
        String questionId = req.getParameter("comment_question_id");

        // answer id
        AnswerId answerId = req.getParameter("comment_answer_id") == null ? null : new AnswerId(
                req.getParameter("comment_answer_id"));


        CommentCommand commentCommand = CommentCommand.builder()
                .createDate(new Date())
                .userId(currentUserDTO.getUserId())
                // If null, it mean the comment is for a question else a answer comment
                .questionId(answerId == null ? new QuestionId(questionId) : null)
                .answerId(answerId)
                .description(req.getParameter("txt_question_comment"))
                .build();


        try {
            commentFacade.addComment(commentCommand);
            resp.sendRedirect(getServletContext().getContextPath() + "/question?id=" + questionId);
        } catch (UserFailedException e) {
            e.printStackTrace();
        }

    }
}
