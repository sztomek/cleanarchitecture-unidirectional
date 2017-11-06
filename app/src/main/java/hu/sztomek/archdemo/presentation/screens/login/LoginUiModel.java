package hu.sztomek.archdemo.presentation.screens.login;

import hu.sztomek.archdemo.presentation.common.UiModel;

class LoginUiModel implements UiModel {

    private final boolean isSuccess;

    public LoginUiModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
