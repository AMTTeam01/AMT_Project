package ch.heigvd.amt.mvcProject.ui.web.question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionRenderer", urlPatterns = "/question")
public class QuestionRenderer extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getAttribute("id");
        request.getRequestDispatcher("/WEB-INF/views/new_question.jsp").forward(request, response);
    }
}
