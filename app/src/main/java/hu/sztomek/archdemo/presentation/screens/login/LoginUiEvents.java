package hu.sztomek.archdemo.presentation.screens.login;

import hu.sztomek.archdemo.presentation.common.UiEvent;

final class LoginUiEvents {

    private LoginUiEvents() {}

    public static GoogleLoginEvent google(String token) {
        return new GoogleLoginEvent(token);
    }

    public static class GoogleLoginEvent implements UiEvent {
        private final String token;

        GoogleLoginEvent(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

}
