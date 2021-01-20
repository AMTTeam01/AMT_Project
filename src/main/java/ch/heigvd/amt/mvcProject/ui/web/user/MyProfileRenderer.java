package ch.heigvd.amt.mvcProject.ui.web.user;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgesDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.ProfileFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.BadgesAwardDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.UsersProfileDTOJSON;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "MyProfileServlet", urlPatterns = "/my_profile")
public class MyProfileRenderer extends HttpServlet {

    @Inject
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;
    private ProfileFacade profileFacade;
    private BadgeFacade badgeFacade;

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
        UsersProfileDTOJSON.UserProfileDTOJSON userProfileDTO = null;

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

        HashMap<String, ArrayList<BadgesAwardDTO.BadgeAwardDTO>> sets = new HashMap<>();

        for(BadgesAwardDTO.BadgeAwardDTO badgeAwardDTO : userProfileDTO.getBadgesAwards()) {
            if(!sets.containsKey(badgeAwardDTO.getPath())) {
                sets.put(badgeAwardDTO.getPath(), new ArrayList<>());
            }

            sets.get(badgeAwardDTO.getPath()).add(badgeAwardDTO);
        }

        String bronzePath = "/badges/1";
        String silverPath = "/badges/2";
        String goldPath = "/badges/3";

        request.setAttribute("questions", questionsDTO);
        request.setAttribute("bronzeBadges", sets.get(bronzePath) == null ? new ArrayList<>() : sets.get(bronzePath));
        request.setAttribute("silverBadges", sets.get(silverPath) == null ? new ArrayList<>() : sets.get(silverPath));
        request.setAttribute("goldBadges", sets.get(goldPath) == null ? new ArrayList<>() : sets.get(goldPath));

        request.getRequestDispatcher("/WEB-INF/views/myprofile.jsp").forward(request, response);
    }
}