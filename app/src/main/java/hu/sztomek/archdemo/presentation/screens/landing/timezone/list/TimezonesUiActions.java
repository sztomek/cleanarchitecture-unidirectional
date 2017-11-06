package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import hu.sztomek.archdemo.domain.common.PaginatedAction;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;

public final class TimezonesUiActions {

    private TimezonesUiActions() {
    }

    public static RefreshAction refresh(String userId, String orderBy, int pageSize) {
        return new RefreshAction(userId, orderBy, pageSize);
    }

    public static LoadMoreAction loadMore(String userId, String orderBy, String lastKey, int pageSize) {
        return new LoadMoreAction(userId, orderBy, lastKey, pageSize);
    }

    public static DeleteTimezoneAction delete(String userId, String timezoneId) {
        return new DeleteTimezoneAction(userId, timezoneId);
    }

    public static class RefreshAction extends PaginatedAction {

        RefreshAction(String uid, String orderBy, int pageSize) {
            super(uid, orderBy, null, pageSize);
        }
    }

    public static class LoadMoreAction extends PaginatedAction {

        LoadMoreAction(String uid, String orderBy, String lastKey, int pageSize) {
            super(uid, orderBy, lastKey, pageSize);
        }

    }

}
