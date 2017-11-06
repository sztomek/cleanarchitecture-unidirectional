package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneEvent;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;

public final class TimezoneEditUiEvents {

    private TimezoneEditUiEvents() {}

    public static GetTimezoneEvent get(String userId, String timezoneId) {
        return new GetTimezoneEvent(userId, timezoneId);
    }

    public static SaveTimezoneEvent save(TimezoneModel model) {
        return new SaveTimezoneEvent(model);
    }

    public static DeleteTimezoneEvent delete(String userId, String timezoneId) {
        return new DeleteTimezoneEvent(userId, timezoneId);
    }

    public static class GetTimezoneEvent implements UiEvent {

        private final String userId;
        private final String timezoneId;

        GetTimezoneEvent(String userId, String timezoneId) {
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

    public static class SaveTimezoneEvent implements UiEvent {

        private final TimezoneModel timezone;

        SaveTimezoneEvent(TimezoneModel timezone) {
            this.timezone = timezone;
        }

        public TimezoneModel getTimezone() {
            return timezone;
        }

        @Override
        public String toString() {
            return "SaveTimezoneEvent{" +
                    "timezone=" + timezone +
                    '}';
        }
    }


}
