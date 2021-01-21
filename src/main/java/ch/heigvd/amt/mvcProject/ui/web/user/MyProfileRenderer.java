package ch.heigvd.amt.mvcProject.ui.web.user;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.ProfileFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.BadgesAwardWithBadgeDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.UsersProfileWithBadgeDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "MyProfileServlet", urlPatterns = "/my_profile")
public class MyProfileRenderer extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;
    private ProfileFacade profileFacade;
    private BadgeFacade badgeFacade;

    // List of the names of the badges
    private String BRONZE_NAME = "Bronze";
    private String SILVER_NAME = "Silver";
    private String GOLD_NAME = "Gold";

    /**
     * Init servlet
     * @param config config for superclass
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questionFacade = serviceRegistry.getQuestionFacade();
        profileFacade = serviceRegistry.getProfileFacade();
        badgeFacade = serviceRegistry.getBadgeFacade();
    }

    /**
     * GET renderer, will retrieve questions from the current user
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object errors = request.getSession().getAttribute("errors");
        request.setAttribute("errors", errors);
        request.getSession().removeAttribute("errors");

        CurrentUserDTO currentUser = (CurrentUserDTO) request.getSession().getAttribute("currentUser");
        request.setAttribute("user", currentUser);
        
        QuestionQuery query = QuestionQuery.builder().userId(currentUser.getUserId()).build();

        QuestionsDTO questionsDTO = null;
        UsersProfileWithBadgeDTO.UserProfileWithBadgeDTO userProfileDTO = null;

        try {
            questionsDTO = questionFacade.getQuestions(query);
        } catch (QuestionFailedException e) {
            e.printStackTrace();
        }

        try {
            userProfileDTO = profileFacade.getProfile(currentUser.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, ArrayList<BadgesAwardWithBadgeDTO.BadgeAwardWithBadgeDTO>> sets = new HashMap<>();

        for(BadgesAwardWithBadgeDTO.BadgeAwardWithBadgeDTO badgeAwardDTO : userProfileDTO.getBadgesAwards()) {

            if(!sets.containsKey(badgeAwardDTO.getBadgeDTO().getName())) {
                sets.put(badgeAwardDTO.getBadgeDTO().getName(), new ArrayList<>());
            }

            sets.get(badgeAwardDTO.getBadgeDTO().getName()).add(badgeAwardDTO);
        }

        request.setAttribute("questions", questionsDTO);
        request.setAttribute("bronzeBadges", sets.get(BRONZE_NAME) == null ? new ArrayList<>() : sets.get(BRONZE_NAME));
        request.setAttribute("silverBadges", sets.get(SILVER_NAME) == null ? new ArrayList<>() : sets.get(SILVER_NAME));
        request.setAttribute("goldBadges", sets.get(GOLD_NAME) == null ? new ArrayList<>() : sets.get(GOLD_NAME));

        request.getRequestDispatcher("/WEB-INF/views/myprofile.jsp").forward(request, response);
    }
}