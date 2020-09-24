package ch.heigvd.amt.mvcProject.presentation.handler;

import ch.heigvd.amt.mvcProject.business.UserService;
import ch.heigvd.amt.mvcProject.model.UserRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LoginRequestHandler", urlPatterns = "/request.login")
public class LoginRequestHandler extends HttpServlet {

    private UserService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserRequest userRequest = UserRequest.builder()
                .username(req.getParameter("username"))
                .email("")
                .password(req.getParameter("password"))
                .build();

        if(service.doesExistUser(userRequest.getUsername(), userRequest.getPassword())){
            resp.sendRedirect(getServletContext().getContextPath());
        }else{
            resp.sendRedirect(getServletContext().getContextPath() + "/login?error=Your e-mail or your password is incorrect");
        }
    }
}
