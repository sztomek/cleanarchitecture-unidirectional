package hu.sztomek.archdemo.presentation.common;

public class FormValidationUiError extends UiError {

    public FormValidationUiError(String message) {
        super(true, message);
    }

    @Override
    public String toString() {
        return "FormValidationUiError{"+super.toString()+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }
}
