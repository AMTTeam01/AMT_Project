package ch.heigvd.amt.mvcProject.ui.web.user.handler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogoutRequestHandler", urlPatterns = "/logout.do")
public class LogoutRequestHandler extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Invalid the session
        HttpSession session = req.getSession();
        session.invalidate();

        // override the user and set the valid timeout to 0
        Cookie cookie = new Cookie("username", "");
        cookie.setMaxAge(0);

        resp.addCookie(cookie);
        resp.sendRedirect("/");

    }
}
