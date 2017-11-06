package hu.sztomek.archdemo.presentation.screens.check_user;

import hu.sztomek.archdemo.presentation.common.UiModel;

class CheckUserUiModel implements UiModel {

    private final String loadingMessage;
    private final @CheckPhases.Phase int phase;

    public CheckUserUiModel(String loadingMessage, @CheckPhases.Phase int phase) {
        this.loadingMessage = loadingMessage;
        this.phase = phase;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public @CheckPhases.Phase int getPhase() {
        return phase;
    }

    public boolean canProceed() {
        return phase == CheckPhases.STATE_EXISTS || phase == CheckPhases.STATE_REGISTERED;
    }
}
