package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import hu.sztomek.archdemo.presentation.common.UiModel;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;

class TimezoneEditUiModel implements UiModel {

    private TimezoneModel timezoneModel;
    private boolean success;

    public TimezoneModel getTimezoneModel() {
        return timezoneModel;
    }

    public void setTimezoneModel(TimezoneModel timezoneModel) {
        this.timezoneModel = timezoneModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimezoneEditUiModel that = (TimezoneEditUiModel) o;

        if (success != that.success) return false;
        return timezoneModel != null ? timezoneModel.equals(that.timezoneModel) : that.timezoneModel == null;
    }

    @Override
    public int hashCode() {
        int result = timezoneModel != null ? timezoneModel.hashCode() : 0;
        result = 31 * result + (success ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimezoneEditUiModel{" +
                "timezoneModel=" + timezoneModel +
                ", success=" + success +
                '}';
    }
}
