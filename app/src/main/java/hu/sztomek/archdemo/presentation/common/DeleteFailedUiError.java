package hu.sztomek.archdemo.presentation.common;

public class DeleteFailedUiError extends UiError {

    public DeleteFailedUiError(String message) {
        super(true, message);
    }

    @Override
    public String toString() {
        return "DeleteFailedUiError{"+ super.toString() +"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

}
