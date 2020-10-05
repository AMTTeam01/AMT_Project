package ch.heigvd.amt.mvcProject.ui.web.user;

import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyProfileServlet", urlPatterns = "/my_profile")
public class MyProfileRenderer extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CurrentUserDTO currentUser = (CurrentUserDTO) request.getSession().getAttribute("currentUser");
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/myprofile.jsp").forward(request, response);
    }
}