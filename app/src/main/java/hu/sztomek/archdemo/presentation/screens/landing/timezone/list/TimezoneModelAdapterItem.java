package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import hu.sztomek.archdemo.presentation.views.AdapterItem;

public class TimezoneModelAdapterItem implements AdapterItem {

    public static int ITEM_TIMEZONE = 1;

    private final TimezoneModel data;

    public TimezoneModelAdapterItem(TimezoneModel data) {
        this.data = data;
    }

    @Override
    public int getItemViewType() {
        return ITEM_TIMEZONE;
    }

    @Override
    public String getKey() {
        return data.getName();
    }

    public TimezoneModel getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimezoneModelAdapterItem that = (TimezoneModelAdapterItem) o;

        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TimezoneModelAdapterItem{" +
                "data=" + data +
                '}';
    }
}
