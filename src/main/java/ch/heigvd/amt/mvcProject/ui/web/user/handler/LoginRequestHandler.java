package ch.heigvd.amt.mvcProject.ui.web.user.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.user.UserCommand;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginRequestHandler", urlPatterns = "/request.login")
public class LoginRequestHandler extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private UserFacade userFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        userFacade = serviceRegistry.getUserFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserCommand command = UserCommand.builder()
                .username(req.getParameter("username"))
                .email("")
                .password(req.getParameter("password"))
                .build();

        if (serviceRegistry.isUserExist(command.getUsername(), command.getPassword())) {
            resp.sendRedirect(getServletContext().getContextPath());
        } else {
            resp.sendRedirect(
                    getServletContext().getContextPath() + "/login?error=Your e-mail or your password is incorrect");
        }
    }
}
