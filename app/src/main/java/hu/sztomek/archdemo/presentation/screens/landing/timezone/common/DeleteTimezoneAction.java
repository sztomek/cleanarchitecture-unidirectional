package hu.sztomek.archdemo.presentation.screens.landing.timezone.common;

import hu.sztomek.archdemo.domain.common.AuthenticatedAction;

public class DeleteTimezoneAction extends AuthenticatedAction {

    private final String timezoneId;

    public DeleteTimezoneAction(String userId, String timezoneId) {
        super(userId);
        this.timezoneId = timezoneId;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DeleteTimezoneAction that = (DeleteTimezoneAction) o;

        return timezoneId != null ? timezoneId.equals(that.timezoneId) : that.timezoneId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (timezoneId != null ? timezoneId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeleteTimezoneAction{" +
                super.toString() +
                ", timezoneId='" + timezoneId + '\'' +
                '}';
    }
}