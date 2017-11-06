package hu.sztomek.archdemo.presentation.screens.landing;

import hu.sztomek.archdemo.data.datasource.IUserManager;
import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.UserProfilePayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.common.UserModel;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class LandingPresenter extends BasePresenter<LandingUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<LandingUiActions.LogoutAction> logoutUseCase;
    private final UseCase<LandingUiActions.GetProfileAction> getUserUseCase;
    private final IUserManager userManager;

    public LandingPresenter(UiState<LandingUiModel> latestState, UseCase<LandingUiActions.LogoutAction> logoutUseCase, UseCase<LandingUiActions.GetProfileAction> getUserUseCase, IUserManager userManager) {
        super(latestState);
        this.logoutUseCase = logoutUseCase;
        this.getUserUseCase = getUserUseCase;
        this.userManager = userManager;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> Observable.merge(
                upstream.ofType(LandingUiEvents.LogoutUiEvent.class)
                    .map(event -> LandingUiActions.logout(event.getMenuId())),
                upstream.ofType(LandingUiEvents.MenuSelectedUiEvent.class)
                    .startWith(LandingUiEvents.menuSelected(getLastState().getData().getSelectedMenuId()))
                    .map(event -> LandingUiActions.getProfile(event.getMenuId(), userManager.getUserId()))
        );
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> Observable.merge(
                upstream.ofType(LandingUiActions.LogoutAction.class)
                    .flatMap(action ->
                        logoutUseCase.performAction(action)
                            .onErrorReturn(throwable -> Result.error(action, throwable))
                            .startWith(Result.loading(action))),
                upstream.ofType(LandingUiActions.GetProfileAction.class)
                    .flatMap(action ->
                        getUserUseCase.performAction(action)
                            .onErrorReturn(throwable -> Result.error(action, throwable))
                            .startWith(Result.loading(action))
                    )
        );
    }

    @Override
    protected UiState<LandingUiModel> accumulate(UiState<LandingUiModel> current, Result latest) {
        UiState.Builder<LandingUiModel> builder = current.toBuilder();
        if (latest.getError() != null) {
            builder.setError(new UiError(true, latest.getError().getMessage() == null ? latest.getError().getClass().getSimpleName() : latest.getError().getMessage()));
            return builder.build();
        }

        builder.setError(null);
        builder.setLoading(latest.isLoading());
        LandingUiModel data = current.getData();
        if (latest.getAction() instanceof LandingUiActions.GetProfileAction) {
            LandingUiActions.GetProfileAction action = (LandingUiActions.GetProfileAction) latest.getAction();
            data.setSelectedMenuId(action.getSelectedScreen());
            UserModel userModel = new UserModel();
            if (latest.getPayload() instanceof UserProfilePayload) {
                UserProfilePayload payload = ((UserProfilePayload) latest.getPayload());
                userModel.setName(payload.getName());
                userModel.setEmail(payload.getEmail());
                userModel.setAvatar(payload.getAvatar());
            }
            data.setUserModel(userModel);
        } else if (latest.getAction() instanceof LandingUiActions.LogoutAction) {
            data.setSelectedMenuId(((LandingUiActions.LogoutAction) latest.getAction()).getSelectedScreen());
        }
        builder.setData(data);
        return builder.build();
    }

}
