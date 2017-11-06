package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneEvent;

final class TimezonesUiEvents {

    private TimezonesUiEvents() {
    }

    public static RefreshUiEvent refresh() {
        return new RefreshUiEvent();
    }

    public static LoadMoreUiEvent loadMore(String lastItemKey) {
        return new LoadMoreUiEvent(lastItemKey);
    }

    public static DeleteTimezoneEvent delete(String userId, String timezoneId) {
        return new DeleteTimezoneEvent(userId, timezoneId);
    }

    public static class RefreshUiEvent implements UiEvent {

        RefreshUiEvent() {
        }

    }

    public static class LoadMoreUiEvent implements UiEvent {

        private final String lastItem;

        LoadMoreUiEvent(String lastItem) {
            this.lastItem = lastItem;
        }

        public String getLastItem() {
            return lastItem;
        }
    }

}
