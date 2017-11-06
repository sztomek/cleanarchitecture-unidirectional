package hu.sztomek.archdemo.presentation.screens.email_login;

import hu.sztomek.archdemo.presentation.common.UiModel;

class EmailLoginUiModel implements UiModel {

    private final boolean isSuccess;

    public EmailLoginUiModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
