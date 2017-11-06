package hu.sztomek.archdemo.presentation.screens.forgot_pw;

import hu.sztomek.archdemo.domain.common.Action;

public final class ForgotPwActions {

    private ForgotPwActions() {}

    public static ForgotPwSubmit submit(String email) {
        return new ForgotPwSubmit(email);
    }

    public static class ForgotPwSubmit implements Action {
        private final String email;

        public ForgotPwSubmit(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}
