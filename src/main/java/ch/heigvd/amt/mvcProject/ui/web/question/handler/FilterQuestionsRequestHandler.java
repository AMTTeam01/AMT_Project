package ch.heigvd.amt.mvcProject.ui.web.question.handler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "FilterQuestionsRequestHandler", urlPatterns = "/filter_questions.do")
public class FilterQuestionsRequestHandler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(getServletContext().getContextPath() + "/browsing?tags=" + req.getParameter("txt_search"));
    }
}
