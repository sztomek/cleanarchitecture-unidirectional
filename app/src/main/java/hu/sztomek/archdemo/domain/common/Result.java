package hu.sztomek.archdemo.domain.common;

public class Result {

    private final Action action;
    private final boolean isLoading;
    private final Throwable error;
    private final Payload payload;

    public static Result idle() {
        return new Result(null, false, null, null);
    }

    public static  Result loading(Action action) {
        return new Result(action, true, null, null);
    }
    public static  Result error(Action action, Throwable throwable) {
        return new Result(action, false, throwable, null);
    }
    public static  Result success(Action action, Payload payload) {
        return new Result(action, false, null, payload);
    }

    Result(Action action, boolean isLoading, Throwable error, Payload payload) {
        this.action = action;
        this.isLoading = isLoading;
        this.error = error;
        this.payload = payload;
    }

    public Action getAction() {
        return action;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public Throwable getError() {
        return error;
    }

    public Payload getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (isLoading != result.isLoading) return false;
        if (action != null ? !action.equals(result.action) : result.action != null) return false;
        if (error != null ? !error.equals(result.error) : result.error != null) return false;
        return payload != null ? payload.equals(result.payload) : result.payload == null;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (isLoading ? 1 : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "action=" + action +
                ", isLoading=" + isLoading +
                ", error=" + error +
                ", payload=" + payload +
                '}';
    }
}
