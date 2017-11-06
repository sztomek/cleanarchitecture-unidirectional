package hu.sztomek.archdemo.presentation.screens.forgot_pw;

import hu.sztomek.archdemo.presentation.common.UiModel;

class ForgotPwUiModel  implements UiModel {

    private final boolean isSuccess;

    public ForgotPwUiModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}