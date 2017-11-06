package hu.sztomek.archdemo.domain.common;

public class RestResponseError extends Throwable {

    public RestResponseError(String message) {
        super(message);
    }
}
