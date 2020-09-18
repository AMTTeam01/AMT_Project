package ch.heigvd.amt.mvcProject.presentation;

import ch.heigvd.amt.mvcProject.business.QuoteGenerator;
import ch.heigvd.amt.mvcProject.model.Quote;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class MyProfileServlet extends javax.servlet.http.HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/myprofile.jsp").forward(request, response);
    }


}
