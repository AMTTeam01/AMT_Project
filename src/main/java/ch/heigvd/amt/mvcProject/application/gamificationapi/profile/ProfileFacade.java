package ch.heigvd.amt.mvcProject.application.gamificationapi.profile;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

public class ProfileFacade {
    private APIUtils utils;

    public ProfileFacade() {

    }

    public ProfileFacade(APIUtils utils) {
        this.utils = utils;
    }

    public UsersProfileDTO.UserProfileDTO getProfile(UserId id) throws Exception {
        return utils.getProfil(id);
    }
}
