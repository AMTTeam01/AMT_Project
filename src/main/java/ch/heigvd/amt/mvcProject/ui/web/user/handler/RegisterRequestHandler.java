package ch.heigvd.amt.mvcProject.ui.web.user.handler;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RegisterRequestHandler", urlPatterns = "/register.do")
public class RegisterRequestHandler extends HttpServlet {

    private ServiceRegistry serviceRegistry;
    private AuthenticationFacade authenticationFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        authenticationFacade = serviceRegistry.getAuthenticationFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("errors");

        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(req.getParameter("txt_username"))
                .email(req.getParameter("txt_email"))
                .clearTxtPassword(req.getParameter("txt_password"))
                .confirmationClearTxtPassword(req.getParameter("txt_cpassword"))
                .build();

        try{
            authenticationFacade.register(registerCommand);
            req.getRequestDispatcher("/login.do").forward(req, resp);
        }catch(RegistrationFailedException e){
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
            resp.sendRedirect("/register");
        }
    }
}
