package hu.sztomek.archdemo.presentation.screens.landing.timezone.common;

import hu.sztomek.archdemo.presentation.common.UiEvent;

public class DeleteTimezoneEvent implements UiEvent {

    private final String userId;
    private final String timezoneId;

    public DeleteTimezoneEvent(String userId, String timezoneId) {
        this.userId = userId;
        this.timezoneId = timezoneId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTimezoneId() {
        return timezoneId;
    }
}
