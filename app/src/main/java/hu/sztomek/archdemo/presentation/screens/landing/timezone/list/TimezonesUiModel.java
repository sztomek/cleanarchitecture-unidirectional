package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import java.util.ArrayList;
import java.util.List;

import hu.sztomek.archdemo.presentation.common.UiModel;
import hu.sztomek.archdemo.presentation.views.AdapterItem;

class TimezonesUiModel implements UiModel {

    private List<AdapterItem> timezones;
    private boolean listReachedEnd;

    public TimezonesUiModel() {
        timezones = new ArrayList<>();
    }

    public List<AdapterItem> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<AdapterItem> timezones) {
        this.timezones = timezones;
    }

    public boolean isListReachedEnd() {
        return listReachedEnd;
    }

    public void setListReachedEnd(boolean listReachedEnd) {
        this.listReachedEnd = listReachedEnd;
    }

    @Override
    public String toString() {
        return "TimezonesUiModel{" +
                "timezones=" + timezones +
                ", listReachedEnd=" + listReachedEnd +
                '}';
    }
}
