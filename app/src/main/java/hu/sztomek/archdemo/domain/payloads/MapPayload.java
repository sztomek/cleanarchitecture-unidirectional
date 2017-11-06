package hu.sztomek.archdemo.domain.payloads;

import java.util.Map;

import hu.sztomek.archdemo.domain.common.Payload;

public class MapPayload<T> implements Payload {

    private final Map<String, T> data;

    public MapPayload(Map<String, T> data) {
        this.data = data;
    }

    public Map<String, T> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPayload<?> that = (MapPayload<?>) o;

        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MapPayload{" +
                "data=" + data +
                '}';
    }
}
