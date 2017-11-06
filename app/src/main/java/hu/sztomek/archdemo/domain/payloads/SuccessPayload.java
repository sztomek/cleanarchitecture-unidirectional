package hu.sztomek.archdemo.domain.payloads;

import hu.sztomek.archdemo.domain.common.Payload;

public class SuccessPayload implements Payload {

    private final boolean isSuccess;

    public SuccessPayload(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuccessPayload that = (SuccessPayload) o;

        return isSuccess == that.isSuccess;
    }

    @Override
    public int hashCode() {
        return (isSuccess ? 1 : 0);
    }

    @Override
    public String toString() {
        return "SuccessPayload{" +
                "isSuccess=" + isSuccess +
                '}';
    }
}
