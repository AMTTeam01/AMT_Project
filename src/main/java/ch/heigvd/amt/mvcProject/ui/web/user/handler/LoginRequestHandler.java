package ch.heigvd.amt.mvcProject.ui.web.user.handler;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginRequestHandler", urlPatterns = "/login.do")
public class LoginRequestHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private AuthenticationFacade authenticationFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        authenticationFacade = serviceRegistry.getAuthenticationFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("errors");

        LoginCommand loginCommand = LoginCommand.builder()
                .username(req.getParameter("txt_username"))
                .clearTxtPassword(req.getParameter("txt_password"))
                .build();

        CurrentUserDTO currentUser = null;

        try {
            currentUser = authenticationFacade.login(loginCommand);
            req.getSession().setAttribute("currentUser", currentUser);
            String targetUrl = (String) req.getSession().getAttribute("targetUrl");
            targetUrl = (targetUrl != null) ? targetUrl : "/browsing";

            // Add coockie
            Cookie c1 = new Cookie("username", currentUser.getUsername());

            resp.addCookie(c1);

            resp.sendRedirect(targetUrl);

        } catch (LoginFailedException e) {
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
            resp.sendRedirect("/login");

        }

    }
}
