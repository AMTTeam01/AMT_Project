package ch.heigvd.amt.mvcProject.ui.web;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UsersPointsDTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "LeaderboardRenderer", urlPatterns = "/leaderboard")
public class LeaderboardRenderer extends HttpServlet {

    @Inject
    APIUtils apiUtils;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        ArrayList<UsersPointsDTO.UserPointDTO> gamifiedUsers = new ArrayList<>();

        try {
            gamifiedUsers = apiUtils.getTop10UserPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("users", gamifiedUsers);
        request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
    }
}
