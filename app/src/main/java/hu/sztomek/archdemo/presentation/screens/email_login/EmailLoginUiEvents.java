package hu.sztomek.archdemo.presentation.screens.email_login;

import hu.sztomek.archdemo.presentation.common.UiEvent;

final class EmailLoginUiEvents {

    private EmailLoginUiEvents() {}

    public static LoginClicked login(String email, String password) {
        return new LoginClicked(email, password);
    }

    public static class LoginClicked implements UiEvent {
        private final String email;
        private final String password;

        LoginClicked(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

}
