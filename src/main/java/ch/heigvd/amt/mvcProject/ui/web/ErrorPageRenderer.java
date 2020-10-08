package ch.heigvd.amt.mvcProject.ui.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(name = "ErrorPageRenderer", urlPatterns = "/error")
public class ErrorPageRenderer extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
