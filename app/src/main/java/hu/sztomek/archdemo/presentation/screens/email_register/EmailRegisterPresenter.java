package hu.sztomek.archdemo.presentation.screens.email_register;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import javax.inject.Inject;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.FormValidationException;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EmailRegisterPresenter extends BasePresenter<EmailRegisterUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<EmailRegisterActions.EmailRegister> useCase;

    @Inject
    public EmailRegisterPresenter(UiState<EmailRegisterUiModel> state, UseCase<EmailRegisterActions.EmailRegister> useCase) {
        super(state);
        this.useCase = useCase;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> upstream.ofType(EmailRegisterUiEvents.RegisterUiEvent.class)
                .map((Function<EmailRegisterUiEvents.RegisterUiEvent, Action>) event -> {
                    return EmailRegisterActions.register(event.getEmail(), event.getPassword(), event.getPasswordConfirm());
                });
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> upstream.ofType(EmailRegisterActions.EmailRegister.class)
                .flatMap(action -> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(action.getEmail()).matches()) {
                        return Observable.just(Result.<Boolean, EmailRegisterActions.EmailRegister>error(action, new FormValidationException("Must enter a valid email address!")));
                    }
                    if (!Pattern.compile("[0-9a-zA-Z]{6,}").matcher(action.getPassword()).matches()) {
                        return Observable.just(Result.<Boolean, EmailRegisterActions.EmailRegister>error(action, new FormValidationException("Password must be at least 6 characters long!")));
                    }
                    if (!TextUtils.equals(action.getPassword(), action.getPasswordConfirm())) {
                        return Observable.just(Result.<Boolean, EmailRegisterActions.EmailRegister>error(action, new FormValidationException("Passwords don't match!")));
                    }

                    return useCase.performAction(action)
                            .onErrorReturn(throwable -> Result.error(action, throwable))
                            .startWith(Result.<Boolean, EmailRegisterActions.EmailRegister>loading(action));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UiState<EmailRegisterUiModel> accumulate(UiState<EmailRegisterUiModel> current, Result result) {
        if (result.isLoading()) {
            return UiState.loading();
        } else {
            if (result.getError() != null) {
                return UiState.error(new UiError(true, result.getError().getMessage()));
            } else if (result.getPayload() != null) {
                return UiState.success(new EmailRegisterUiModel(((SuccessPayload) result.getPayload()).isSuccess()));
            } else {
                return UiState.idle();
            }
        }
    }

}
