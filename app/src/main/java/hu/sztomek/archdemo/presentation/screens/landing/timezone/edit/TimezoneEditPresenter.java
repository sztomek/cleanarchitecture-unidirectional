package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.TimezonePayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.DeleteFailedUiError;
import hu.sztomek.archdemo.presentation.common.FormValidationException;
import hu.sztomek.archdemo.presentation.common.FormValidationUiError;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneEvent;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class TimezoneEditPresenter extends BasePresenter<TimezoneEditUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<TimezoneEditUiActions.GetTimezoneAction> getUseCase;
    private final UseCase<TimezoneEditUiActions.SaveTimezoneAction> saveUseCase;
    private final UseCase<TimezoneEditUiActions.SaveTimezoneAction> updateUseCase;
    private final UseCase<DeleteTimezoneAction> deleteUseCase;

    public TimezoneEditPresenter(UiState<TimezoneEditUiModel> latestState,
                                 UseCase<TimezoneEditUiActions.GetTimezoneAction> getUseCase,
                                 UseCase<TimezoneEditUiActions.SaveTimezoneAction> saveUseCase,
                                 UseCase<TimezoneEditUiActions.SaveTimezoneAction> updateUseCase,
                                 UseCase<DeleteTimezoneAction> deleteUseCase) {
        super(latestState);
        this.getUseCase = getUseCase;
        this.saveUseCase = saveUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> Observable.merge(
                upstream.ofType(TimezoneEditUiEvents.GetTimezoneEvent.class)
                    .map(event -> TimezoneEditUiActions.get(event.getUserId(),event.getTimezoneId())),
                upstream.ofType(TimezoneEditUiEvents.SaveTimezoneEvent.class)
                    .map(event -> {
                        final TimezoneEditUiActions.SaveTimezoneAction.Builder builder = new TimezoneEditUiActions.SaveTimezoneAction.Builder();
                        final TimezoneModel tz = event.getTimezone();
                        builder.setCity(tz.getCity());
                        builder.setDifference(tryParseDifference(tz.getDifference()));
                        builder.setId(tz.getId());
                        builder.setName(tz.getName());
                        builder.setUserId(tz.getOwnerId());
                        return TimezoneEditUiActions.save(builder);
                    }),
                upstream.ofType(DeleteTimezoneEvent.class)
                    .map(event -> TimezoneEditUiActions.delete(event.getUserId(), event.getTimezoneId()))
        );
    }

    private int tryParseDifference(String difference) {
        if (difference == null || difference.isEmpty()) {
            return 0;
        }
        int diff;
        try {
            diff = Integer.parseInt(difference);
        } catch (NumberFormatException e) {
            diff = Integer.MIN_VALUE;
        }
        return diff;
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> Observable.merge(
                upstream.ofType(TimezoneEditUiActions.GetTimezoneAction.class)
                    .flatMap(action -> getUseCase.performAction(action)
                        .onErrorReturn(t -> Result.error(action, t))
                        .startWith(Result.loading(action))
                    ),
                upstream.ofType(TimezoneEditUiActions.SaveTimezoneAction.class)
                    .flatMap(action -> {
                        if (action.getName() == null || action.getName().isEmpty()) {
                            return Observable.just(Result.error(action, new FormValidationException("Name cannot be empty!")));
                        } else if (action.getCity() == null || action.getCity().isEmpty()) {
                            return Observable.just(Result.error(action, new FormValidationException("City cannot be empty!")));
                        } else if (action.getDifference() < -12 || action.getDifference() > 12) {
                            return Observable.just(Result.error(action, new FormValidationException("Difference must be an integer between -12 and +12!")));
                        }
                        final Observable<Result> result;
                        if (action.getId() == null) {
                            result = saveUseCase.performAction(action);
                        } else {
                            result = updateUseCase.performAction(action);
                        }
                        return result
                                .onErrorReturn(t -> Result.error(action, t))
                                .startWith(Result.loading(action));
                    }),
                upstream.ofType(DeleteTimezoneAction.class)
                    .flatMap(action -> deleteUseCase.performAction(action)
                            .onErrorReturn(t -> Result.error(action, t))
                            .startWith(Result.loading(action))
                    )
        );
    }

    @Override
    protected UiState<TimezoneEditUiModel> accumulate(UiState<TimezoneEditUiModel> current, Result result) {
        final UiState.Builder<TimezoneEditUiModel> builder = current.toBuilder();
        builder.setLoading(result.isLoading());
        builder.setError(null);

        if (result.getError() != null) {
            if (result.getAction() instanceof DeleteTimezoneAction) {
                builder.setError(new DeleteFailedUiError("Failed to delete item. Try again later"));
            } else if (result.getError() instanceof FormValidationException) {
                builder.setError(new FormValidationUiError(result.getError().getMessage()));
            } else {
                builder.setError(new UiError(true, result.getError().getMessage() == null ? result.getError().getClass().getSimpleName() : result.getError().getMessage()));
            }

            return builder.build();
        }

        final TimezoneEditUiModel model = new TimezoneEditUiModel();
        if (result.getAction() instanceof TimezoneEditUiActions.GetTimezoneAction) {
            model.setSuccess(false);
            if (result.getPayload() instanceof TimezonePayload) {
                final TimezonePayload payload = (TimezonePayload) result.getPayload();
                final TimezoneModel tzm = new TimezoneModel();
                final TimezoneEditUiActions.GetTimezoneAction action = (TimezoneEditUiActions.GetTimezoneAction) result.getAction();
                tzm.setOwnerId(action.getUid());
                tzm.setId(action.getTimezoneId());
                tzm.setCity(payload.getCity());
                tzm.setName(payload.getName());
                tzm.setDifference(String.valueOf(payload.getDifference()));
                model.setTimezoneModel(tzm);
            }
        } else {
            model.setTimezoneModel(current.getData().getTimezoneModel());
            model.setSuccess(!result.isLoading());
        }
        builder.setData(model);

        return builder.build();
    }
}
