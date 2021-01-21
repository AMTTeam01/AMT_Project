package ch.heigvd.amt.mvcProject.ui.web;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale.PointScaleDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.pointScale.PointScaleUsernameDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UserPointsDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.user.UsernamePointsDTO;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "LeaderboardRenderer", urlPatterns = "/leaderboard")
public class LeaderboardRenderer extends HttpServlet {

    @Inject
    APIUtils apiUtils;

    @Inject
    private IUserRepository userRepository;

    public LeaderboardRenderer() {
    }

    public LeaderboardRenderer(APIUtils apiUtils, IUserRepository userRepository) {
        this.apiUtils = apiUtils;
        this.userRepository = userRepository;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {

        // Get all users in top 10 leaderboard
        List<PointScaleDTO> pointScales = new ArrayList<>();
        try {
            pointScales = apiUtils.getTop10UserPointScales();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the username version for display
        List<PointScaleUsernameDTO> pointScalesUsername = new ArrayList<>();
        for(PointScaleDTO pointScale : pointScales) {
            // For each point scale we create the version with usernames
            PointScaleUsernameDTO pointScaleUsername = PointScaleUsernameDTO
                    .builder()
                    .name(pointScale.getName())
                    .build();
            // Add all the username in that pointscale
            List<UsernamePointsDTO> usernamesPoints = new ArrayList<>();
            for(UserPointsDTO userPoints : pointScale.getLeaderboard()) {
                UsernamePointsDTO usernamePoints = UsernamePointsDTO
                        .builder()
                        .points(userPoints.getTotalPoints())
                        .build();
                System.out.println("Getting the user with id : " + userPoints.getUserId());
                Optional<User> user = userRepository.findById(new UserId(userPoints.getUserId()));
                if(user.isPresent()) {
                    usernamePoints.setUsername(user.get().getUsername());
                    usernamesPoints.add(usernamePoints);
                }
            }
            pointScaleUsername.setLeaderboard(usernamesPoints);
            pointScalesUsername.add(pointScaleUsername);
        }

        request.setAttribute("pointscales", pointScalesUsername);
        request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
    }
}
