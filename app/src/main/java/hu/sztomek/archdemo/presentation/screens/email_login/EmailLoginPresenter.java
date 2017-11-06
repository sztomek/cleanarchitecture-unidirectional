package hu.sztomek.archdemo.presentation.screens.email_login;

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

public class EmailLoginPresenter extends BasePresenter<EmailLoginUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<EmailLoginActions.LoginAction> useCase;

    @Inject
    public EmailLoginPresenter(UiState<EmailLoginUiModel> latestState, UseCase<EmailLoginActions.LoginAction> useCase) {
        super(latestState);
        this.useCase = useCase;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> upstream
                .ofType(EmailLoginUiEvents.LoginClicked.class)
                .map((Function<EmailLoginUiEvents.LoginClicked, Action>) loginClicked -> EmailLoginActions.login(loginClicked.getEmail(), loginClicked.getPassword()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> {

            final Observable<EmailLoginActions.LoginAction> loginActionObservable = upstream
                    .ofType(EmailLoginActions.LoginAction.class);
            return loginActionObservable
                    .flatMap(loginAction -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(loginAction.getEmail()).matches()) {
                            return Observable.just(Result.<Boolean, EmailLoginActions.LoginAction>error(loginAction, new FormValidationException("Must enter a valid email address!")));
                        }
                        if (!Pattern.compile("[0-9a-zA-Z]{6,}").matcher(loginAction.getPassword()).matches()) {
                            return Observable.just(Result.<Boolean, EmailLoginActions.LoginAction>error(loginAction, new FormValidationException("Password must be at least 6 characters long!")));
                        }
                        return useCase.performAction(loginAction)
                                .onErrorReturn(throwable -> Result.error(loginAction, throwable))
                                .startWith(Result.<Boolean, EmailLoginActions.LoginAction>loading(loginAction));
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

        };
    }

    @Override
    protected UiState<EmailLoginUiModel> accumulate(UiState<EmailLoginUiModel> current, Result latest) {
        if (latest.isLoading()) {
            return UiState.loading();
        } else {
            if (latest.getError() != null) {
                return UiState.error(new UiError(true, latest.getError().getMessage() == null ? latest.getError().getClass().getSimpleName() : latest.getError().getMessage()));
            } else if (latest.getPayload() != null) {
                return UiState.success(new EmailLoginUiModel(((SuccessPayload) latest.getPayload()).isSuccess()));
            } else {
                return UiState.idle();
            }
        }
    }

}
