package hu.sztomek.archdemo.presentation.screens.email_register;

import hu.sztomek.archdemo.presentation.common.UiModel;

class EmailRegisterUiModel implements UiModel {

    private final boolean success;

    public EmailRegisterUiModel(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
