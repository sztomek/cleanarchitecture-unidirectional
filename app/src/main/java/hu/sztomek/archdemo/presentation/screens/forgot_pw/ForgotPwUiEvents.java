package hu.sztomek.archdemo.presentation.screens.forgot_pw;

import hu.sztomek.archdemo.presentation.common.UiEvent;

final class ForgotPwUiEvents {

    public static SubmitEvent submit(String email) {
        return new SubmitEvent(email);
    }

    public static class SubmitEvent implements UiEvent {

        private final String email;

        SubmitEvent(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

}
