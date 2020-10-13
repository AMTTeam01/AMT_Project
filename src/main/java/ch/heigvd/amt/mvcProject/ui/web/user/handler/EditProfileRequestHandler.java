package ch.heigvd.amt.mvcProject.ui.web.user.handler;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.edit.EditFailedException;
import ch.heigvd.amt.mvcProject.application.user.edit.EditUserCommand;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EditProfileRequestHandler", urlPatterns = "/edit_profile.do")
public class EditProfileRequestHandler extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private UserFacade editFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        editFacade = serviceRegistry.getUserFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().removeAttribute("errors");

        // Get the current user for obtaining the id of current connected user

        CurrentUserDTO currentUser = (CurrentUserDTO) req.getSession().getAttribute("currentUser");

        EditUserCommand command = EditUserCommand.builder()
                .id(currentUser.getUserId().asString())
                .email(req.getParameter("txt_email"))
                .username(req.getParameter("txt_username"))
                .password(req.getParameter("txt_password"))
                .confirmationPassword(req.getParameter("txt_cpassword")).build();

        try{
            editFacade.editUser(command);
            req.getRequestDispatcher("/my_profile").forward(req, resp);
        }catch(EditFailedException e){
            req.getSession().setAttribute("errors", List.of(e.getMessage()));
            resp.sendRedirect("/edit_profile");
        }
    }
}
