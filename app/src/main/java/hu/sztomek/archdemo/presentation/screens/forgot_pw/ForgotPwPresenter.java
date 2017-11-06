package hu.sztomek.archdemo.presentation.screens.forgot_pw;

import android.util.Patterns;

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

public class ForgotPwPresenter extends BasePresenter<ForgotPwUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<ForgotPwActions.ForgotPwSubmit> useCase;

    @Inject
    public ForgotPwPresenter(UiState<ForgotPwUiModel> lastState, UseCase<ForgotPwActions.ForgotPwSubmit> useCase) {
        super(lastState);
        this.useCase = useCase;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> upstream.ofType(ForgotPwUiEvents.SubmitEvent.class)
                .map((Function<ForgotPwUiEvents.SubmitEvent, Action>) submitEvent -> {
                    return ForgotPwActions.submit(submitEvent.getEmail());
                });
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> upstream.ofType(ForgotPwActions.ForgotPwSubmit.class)
                .flatMap(action -> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(action.getEmail()).matches()) {
                        return Observable.just(Result.<Boolean, ForgotPwActions.ForgotPwSubmit>error(action, new FormValidationException("Must enter a valid email address!")));
                    }

                    return useCase.performAction(action)
                            .onErrorReturn(throwable -> Result.error(action, throwable))
                            .startWith(Result.<Boolean, ForgotPwActions.ForgotPwSubmit>loading(action));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UiState<ForgotPwUiModel> accumulate(UiState<ForgotPwUiModel> current, Result result) {
        if (result.isLoading()) {
            return UiState.loading();
        } else {
            if (result.getError() != null) {
                return UiState.error(new UiError(true, result.getError().getMessage() == null ? result.getError().getClass().getSimpleName() : result.getError().getMessage()));
            } else if (result.getPayload() != null) {
                return UiState.success(new ForgotPwUiModel(((SuccessPayload) result.getPayload()).isSuccess()));
            } else {
                return UiState.idle();
            }
        }
    }

}
