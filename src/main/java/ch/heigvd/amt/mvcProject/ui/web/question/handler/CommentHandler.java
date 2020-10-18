package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.comment.CommentCommand;
import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "CommentHandler", urlPatterns = "/comment.do")
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().removeAttribute("errors");

        CommentCommand commentCommand = CommentCommand.builder()
                .createDate(new Date())
                .description(req.getParameter("txt_comment"))
                .build();

        commentFacade.addComment(commentCommand);
        resp.sendRedirect( getServletContext().getContextPath() + "/question?id=" + req.getParameter("hidden_id"));
    }
}
