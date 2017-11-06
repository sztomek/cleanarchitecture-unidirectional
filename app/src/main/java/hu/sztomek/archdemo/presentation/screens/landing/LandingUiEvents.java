package hu.sztomek.archdemo.presentation.screens.landing;

import hu.sztomek.archdemo.presentation.common.UiEvent;

final class LandingUiEvents {

    private LandingUiEvents() {}

    public static LogoutUiEvent logout(int menuId) {
        return new LogoutUiEvent(menuId);
    }

    public static MenuSelectedUiEvent menuSelected(int menuId) {
        return new MenuSelectedUiEvent(menuId);
    }

    public static class MenuSelectedUiEvent implements UiEvent {

        private final int menuId;

        MenuSelectedUiEvent(int menuId) {
            this.menuId = menuId;
        }

        public int getMenuId() {
            return menuId;
        }
    }

    public static class LogoutUiEvent extends MenuSelectedUiEvent {

        LogoutUiEvent(int menuId) {
            super(menuId);
        }
    }

}
