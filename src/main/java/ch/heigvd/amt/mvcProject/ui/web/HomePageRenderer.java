package ch.heigvd.amt.mvcProject.ui.web;

import ch.heigvd.amt.mvcProject.APIUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

// Default page : https://stackoverflow.com/questions/20455442/java-servlet-specify-start-page-wih-webservlet-annotation

@WebServlet(name = "HomePageRenderer", urlPatterns = "/index.html")
public class HomePageRenderer extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // TEMP : APIUtils Usage (once we implement in the website)
        try {
            APIUtils.register();
            APIUtils.createPointScale("Test", "This is a test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
