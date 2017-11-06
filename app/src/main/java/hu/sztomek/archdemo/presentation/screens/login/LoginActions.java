package hu.sztomek.archdemo.presentation.screens.login;

import hu.sztomek.archdemo.domain.common.Action;

public class LoginActions implements Action {

    private LoginActions() {}

    public static GoogleLoginAction google(String token) {
        return new GoogleLoginAction(token);
    }

    public static  class GoogleLoginAction implements Action {

        private final String token;

        GoogleLoginAction(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
