package ch.heigvd.amt.mvcProject.application.gamificationapi.badge;

import ch.heigvd.amt.mvcProject.APIUtils;

import java.util.ArrayList;

public class BadgeFacade {
    private APIUtils utils;

    public BadgeFacade() {
    }

    public BadgeFacade(APIUtils utils) {
        this.utils = utils;
    }

    public ArrayList<BadgesDTO.BadgeDTO> getBadges() throws Exception {
        return utils.getBadges();
    }
}
