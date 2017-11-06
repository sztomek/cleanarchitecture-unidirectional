package hu.sztomek.archdemo.presentation.common;

public class UiError {

    private final boolean isRecoverable;
    private final String message;

    public UiError(boolean isRecoverable, String message) {
        this.isRecoverable = isRecoverable;
        this.message = message;
    }

    public boolean isRecoverable() {
        return isRecoverable;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UiError uiError = (UiError) o;

        return (isRecoverable == uiError.isRecoverable);
    }

    @Override
    public int hashCode() {
        int result = (isRecoverable ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "isRecoverable=" + isRecoverable +
                ", message='" + message + '\'' +
                '}';
    }
}
