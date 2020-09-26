package ch.heigvd.amt.mvcProject.ui.web.user;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.user.UserCommand;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterRequestHandler", urlPatterns = "/request.register")
public class RegisterRequestHandler extends HttpServlet {

    private ServiceRegistry serviceRegistry = ServiceRegistry.getServiceRegistry();
    private UserFacade userFacade = serviceRegistry.getUserFacade();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String password = req.getParameter("password");
        String cPasssword = req.getParameter("cPassword");

        if (password.equals(cPasssword)) {

            UserCommand command = UserCommand.builder()
                    .username(req.getParameter("username"))
                    .email(req.getParameter("email"))
                    .password(password)
                    .build();


            userFacade.addNewUser(command);

            resp.sendRedirect(getServletContext().getContextPath());

        } else {
            resp.sendRedirect(getServletContext()
                    .getContextPath() + "/register?error='Your password and your confirmation aren't the same'");
        }
    }
}
