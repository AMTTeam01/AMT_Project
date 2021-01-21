package ch.heigvd.amt.mvcProject.application.gamificationapi.profile;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgesDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.BadgesAwardWithBadgeDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.UsersProfileDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json.UsersProfileWithBadgeDTO;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProfileFacade {
    private APIUtils utils;

    public ProfileFacade() {

    }

    public ProfileFacade(APIUtils utils) {
        this.utils = utils;
    }

    public UsersProfileWithBadgeDTO.UserProfileWithBadgeDTO getProfile(UserId id) throws Exception {
        UsersProfileDTO.UserProfileDTO userProfileDTO = utils.getProfile(id);
        Gson gson = new Gson();

        ArrayList<BadgesAwardWithBadgeDTO.BadgeAwardWithBadgeDTO> badgeAwardWithBadgeDTOS = new ArrayList<>();

        userProfileDTO.getBadgesAwards().forEach(val -> {
            try {
                BadgesDTO.BadgeDTO badgeDTO = gson.fromJson(utils.doGetRequestWithString(val.getPath()), BadgesDTO.BadgeDTO.class);
                badgeAwardWithBadgeDTOS.add(BadgesAwardWithBadgeDTO.BadgeAwardWithBadgeDTO.builder()
                        .badgeDTO(badgeDTO)
                        .reason(val.getReason())
                        .timestamp(val.getTimestamp())
                        .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return UsersProfileWithBadgeDTO.UserProfileWithBadgeDTO.builder()
                .badgesAmount(userProfileDTO.getBadgesAmount())
                .badgesAwards(badgeAwardWithBadgeDTOS)
                .pointsAwards(userProfileDTO.getPointsAwards())
                .pointScalesAmount(userProfileDTO.getPointScalesAmount())
                .id(userProfileDTO.getId())
                .build();
    }
}
