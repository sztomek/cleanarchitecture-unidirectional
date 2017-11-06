package hu.sztomek.archdemo.presentation.screens.email_register;

import hu.sztomek.archdemo.domain.common.Action;

public final class EmailRegisterActions {

    private EmailRegisterActions() {}

    public static EmailRegister register(String email, String pw, String pwConfirm) {
        return new EmailRegister(email, pw, pwConfirm);
    }

    public static class EmailRegister implements Action {
        private final String email;
        private final String password;
        private final String passwordConfirm;

        EmailRegister(String email, String password, String passwordConfirm) {
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
