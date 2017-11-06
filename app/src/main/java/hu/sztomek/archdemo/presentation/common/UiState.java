package hu.sztomek.archdemo.presentation.common;

public final class UiState<M extends UiModel> {

    public static <M extends UiModel> UiState<M> loading() {
        return new UiState<>(true, null, null);
    }

    public static <M extends UiModel> UiState<M> success(M data) {
        return new UiState<>(false, null, data);
    }

    public static <M extends UiModel> UiState<M> error(UiError error) {
        return new UiState<>(false, error, null);
    }

    public static <M extends UiModel> UiState<M> idle() {
        return new UiState<>(false, null, null);
    }

    private final boolean isLoading;
    private final UiError error;
    private final M data;

    UiState(boolean isLoading, UiError error, M data) {
        this.isLoading = isLoading;
        this.error = error;
        this.data = data;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public UiError getError() {
        return error;
    }

    public M getData() {
        return data;
    }

    public Builder<M> toBuilder() {
        return new Builder<M>()
                .setData(data)
                .setLoading(isLoading)
                .setError(error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UiState<?> uiState = (UiState<?>) o;

        if (isLoading != uiState.isLoading) return false;
        if (error != null ? !error.equals(uiState.error) : uiState.error != null) return false;
        return data != null ? data.equals(uiState.data) : uiState.data == null;
    }

    @Override
    public int hashCode() {
        int result = (isLoading ? 1 : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UiState{" +
                "isLoading=" + isLoading +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Builder<U extends UiModel> {

        private boolean isLoading;
        private UiError error;
        private U data;

        public Builder<U> setLoading(boolean loading) {
            isLoading = loading;
            return this;
        }

        public Builder<U> setError(UiError error) {
            this.error = error;
            return this;
        }

        public Builder<U> setData(U data) {
            this.data = data;
            return this;
        }

        public UiState<U> build() {
            return new UiState<>(isLoading, error, data);
        }
    }
}
