package hu.sztomek.archdemo.presentation.screens.landing;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.AuthenticatedAction;

public final class LandingUiActions {

    private LandingUiActions() {}

    public static LogoutAction logout(int selectedScreen) {
        return new LogoutAction(selectedScreen);
    }

    public static GetProfileAction getProfile(int selectedScreen, String userId) {
        return new GetProfileAction(selectedScreen, userId);
    }

    public static class LogoutAction implements Action {

        private final int selectedScreen;

        LogoutAction(int selectedScreen) {
            this.selectedScreen = selectedScreen;
        }

        public int getSelectedScreen() {
            return selectedScreen;
        }
    }

    public static class GetProfileAction extends AuthenticatedAction {

        private final int selectedScreen;

        GetProfileAction(int selectedScreen, String userId) {
            super(userId);
            this.selectedScreen = selectedScreen;
        }

        public int getSelectedScreen() {
            return selectedScreen;
        }

    }

}
