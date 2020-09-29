package ch.heigvd.amt.mvcProject.ui.web.user.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginRequestHandler", urlPatterns = "/login.do")
public class LoginRequestHandler extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private AuthenticationFacade authenticationFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        authenticationFacade = serviceRegistry.getUserFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("errors");

        LoginCommand loginCommand = LoginCommand.builder()
                .username(req.getParameter("txt_username"))
                .clearTxtPassword(req.getParameter("txt_password"))
                .build();

<<<<<<< HEAD
        CurrentUserDTO currentUser = null;

        try{
            currentUser = authenticationFacade.login(loginCommand);
            req.getSession().setAttribute("currentUser", currentUser);
            String targetUrl = (String) req.getSession().getAttribute("targetUrl");
            targetUrl = (targetUrl != null) ? targetUrl : "/browse"; // TODO : à changer en fonction de l'URL choisie
            resp.sendRedirect(targetUrl);

        } catch (LoginFailedException e) {
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
            resp.sendRedirect("/login");
=======
        if (serviceRegistry.hasUser(command.getUsername(), command.getPassword())) {
            resp.sendRedirect(getServletContext().getContextPath());
        } else {
            resp.sendRedirect(
                    getServletContext().getContextPath() + "/login?error=Your e-mail or your password is incorrect");
>>>>>>> fb_design_new_question
        }
    }
}
