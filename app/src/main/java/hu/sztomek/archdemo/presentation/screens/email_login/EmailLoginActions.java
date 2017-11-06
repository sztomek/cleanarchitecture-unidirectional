package hu.sztomek.archdemo.presentation.screens.email_login;

import hu.sztomek.archdemo.domain.common.Action;

public final class EmailLoginActions {

    private EmailLoginActions() {}

    public static LoginAction login(String email, String password) {
        return new LoginAction(email, password);
    }

    public static EmailRegisterAction register(String email) {
        return new EmailRegisterAction(email);
    }

    public static EmailForgotPwAction forgotPw(String email) {
        return new EmailForgotPwAction(email);
    }

    public static class LoginAction implements Action {

        private final String email;
        private final String password;

        LoginAction(String email, String password) {
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

    public static class EmailRegisterAction implements Action {

        private final String email;

        EmailRegisterAction(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class EmailForgotPwAction implements Action {

        private final String email;

        EmailForgotPwAction(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

}
