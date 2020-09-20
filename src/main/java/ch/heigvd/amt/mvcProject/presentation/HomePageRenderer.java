package ch.heigvd.amt.mvcProject.presentation;

import ch.heigvd.amt.mvcProject.business.SloganGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class HomePageRenderer extends HttpServlet {

    private SloganGenerator service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = new SloganGenerator();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        String model = service.generateSlogan();
        request.setAttribute("home", model);
        //System.out.println(service.generateSlogan());
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
