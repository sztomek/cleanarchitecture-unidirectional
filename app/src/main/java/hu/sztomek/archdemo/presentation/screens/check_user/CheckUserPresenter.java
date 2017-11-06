package hu.sztomek.archdemo.presentation.screens.check_user;

import hu.sztomek.archdemo.data.datasource.IUserManager;
import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class CheckUserPresenter extends BasePresenter<CheckUserUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<CheckUserActions.CheckUserExists> checkUserUseCase;
    private final UseCase<CheckUserActions.RegisterUserRecord> registerUserUseCase;
    private final IUserManager userManager;

    public CheckUserPresenter(UiState latestState, UseCase<CheckUserActions.CheckUserExists> checkUserUseCase, UseCase<CheckUserActions.RegisterUserRecord> registerUseCase, IUserManager userManager) {
        super(latestState);
        this.userManager = userManager;
        setLastState(latestState == null
                ? UiState.<CheckUserUiModel>idle().toBuilder().setData(new CheckUserUiModel("Please wait...", CheckPhases.STATE_START)).build()
                : latestState);
        this.checkUserUseCase = checkUserUseCase;
        this.registerUserUseCase = registerUseCase;
    }

    @Override
    public void attach(Contract.View view) {
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }

        ConnectableObservable<UiEvent> publish = view.actionStream().publish();
        disposables.add(
                publish
                        .startWith(new CheckUserUiEvents.CheckUserUiEvent())
                        .compose(eventsToActions())
                        .compose(actionsToResults())
                        .distinct()
                        .scan(getLastState(), this::accumulate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                checkUserUiModel -> setLastState(checkUserUiModel),
                                throwable -> setLastState(getLastState().toBuilder().setError(new UiError(true, throwable.toString())).build())
                        )
        );

        publish.connect();
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> upstream
                .map(event -> getLastState().getData() == null || getLastState().getData().getPhase() == CheckPhases.STATE_START
                        ? CheckUserActions.exists(userManager.getUserId())
                        : CheckUserActions.createRecord(userManager.getUserId(), userManager.getUserName(), userManager.getUserEmail(), userManager.getAvatar()));
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> Observable.merge(
                upstream.ofType(CheckUserActions.CheckUserExists.class)
                        .flatMap(checkUserExists -> checkUserUseCase.performAction(checkUserExists)
                                .onErrorReturn(throwable -> Result.error(checkUserExists, throwable))
                                .startWith(Result.<Boolean, CheckUserActions.CheckUserExists>loading(checkUserExists))),
                upstream.ofType(CheckUserActions.RegisterUserRecord.class)
                        .flatMap(action -> registerUserUseCase.performAction(action)
                                .onErrorReturn(throwable -> Result.error(action, throwable)))
        );
    }

    @Override
    protected UiState<CheckUserUiModel> accumulate(UiState<CheckUserUiModel> current, Result latest) {
            final UiState.Builder<CheckUserUiModel> builder = current.toBuilder();
            builder.setLoading(latest.isLoading());

            if (latest.getError() != null) {
                builder.setError(new UiError(true, latest.getError().getMessage() == null ? latest.getError().getClass().getSimpleName() : latest.getError().getMessage()));
                return builder.build();
            }

            if (latest.getPayload() instanceof SuccessPayload) {
                final boolean success = ((SuccessPayload) latest.getPayload()).isSuccess();
                final String loadingMessage;
                final @CheckPhases.Phase int phase;
                if (latest.getAction() instanceof CheckUserActions.CheckUserExists) {
                    loadingMessage = "Checking if user exists...";
                    phase = success ? CheckPhases.STATE_EXISTS : CheckPhases.STATE_DOESNT_EXISTS;
                } else if (latest.getAction() instanceof CheckUserActions.RegisterUserRecord) {
                    loadingMessage = "Registering user in database...";
                    if (success) {
                        phase = CheckPhases.STATE_REGISTERED;
                    } else {
                        builder.setError(new UiError(true, "Unknown error during registration"));
                        phase = CheckPhases.STATE_REGISTERING_IN_DB;
                        return builder.build();
                    }
                } else {
                    loadingMessage = "Please wait...";
                    phase = getLastState().getData().getPhase();
                }

                builder.setData(new CheckUserUiModel(loadingMessage, phase));
            }

        return builder.build();
    }
}
