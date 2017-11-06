package hu.sztomek.archdemo.presentation.screens.email_register;

import hu.sztomek.archdemo.presentation.common.UiEvent;

final class EmailRegisterUiEvents {

    private EmailRegisterUiEvents() {}

    public static RegisterUiEvent register(String email, String pw, String pwConfirm) {
        return new RegisterUiEvent(email, pw, pwConfirm);
    }

    public static class RegisterUiEvent implements UiEvent {
        private final String email;
        private final String password;
        private final String passwordConfirm;

        RegisterUiEvent(String email, String password, String passwordConfirm) {
            this.email = email;
            this.password = password;
            this.passwordConfirm = passwordConfirm;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }
    }
}
