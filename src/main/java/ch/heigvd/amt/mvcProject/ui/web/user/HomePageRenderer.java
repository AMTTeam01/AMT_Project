package ch.heigvd.amt.mvcProject.ui.web.user;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(name = "HomePageRenderer", urlPatterns = "/home")
public class HomePageRenderer extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
