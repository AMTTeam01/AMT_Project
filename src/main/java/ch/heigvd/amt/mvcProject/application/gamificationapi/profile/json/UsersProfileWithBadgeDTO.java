package ch.heigvd.amt.mvcProject.application.gamificationapi.profile.json;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class UsersProfileWithBadgeDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class UserProfileWithBadgeDTO {
        private String id;
        private ArrayList<BadgesAmountDTO.BadgeAmountDTO> badgesAmount;
        private ArrayList<PointscalesAmountDTO.PointscaleAmountDTO> pointScalesAmount;
        private ArrayList<BadgesAwardWithBadgeDTO.BadgeAwardWithBadgeDTO> badgesAwards;
        private ArrayList<PointsAwardsDTO.PointAwardDTO> pointsAwards;
    }

    @Singular
    private List<UserProfileWithBadgeDTO> userProfileDTOS;
}
