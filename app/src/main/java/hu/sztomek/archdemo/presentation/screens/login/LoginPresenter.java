package hu.sztomek.archdemo.presentation.screens.login;

import javax.inject.Inject;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class LoginPresenter extends BasePresenter<LoginUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<LoginActions.GoogleLoginAction> useCase;

    @Inject
    public LoginPresenter(UiState<LoginUiModel> lastState, UseCase<LoginActions.GoogleLoginAction> useCase) {
        super(lastState);
        this.useCase = useCase;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> upstream
                .ofType(LoginUiEvents.GoogleLoginEvent.class)
                .map((Function<LoginUiEvents.GoogleLoginEvent, Action>) googleLoginEvent -> LoginActions.google(googleLoginEvent.getToken()));
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> upstream.ofType(LoginActions.GoogleLoginAction.class)
                .flatMap(googleLoginAction -> useCase.performAction(googleLoginAction)
                        .onErrorReturn(throwable -> Result.error(googleLoginAction, throwable))
                        .startWith(Result.loading(googleLoginAction)));
    }

    @Override
    protected UiState<LoginUiModel> accumulate(UiState<LoginUiModel> current, Result result) {
        if (result.isLoading()) {
            return UiState.loading();
        } else {
            if (result.getError() != null) {
                return UiState.error(new UiError(true, result.getError().getMessage() == null ? result.getError().getClass().getSimpleName() : result.getError().getMessage()));
            } else if (result.getPayload() != null) {
                return UiState.success(new LoginUiModel(((SuccessPayload) result.getPayload()).isSuccess()));
            } else {
                return UiState.idle();
            }
        }
    }

}
