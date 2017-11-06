package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.sztomek.archdemo.data.datasource.IUserManager;
import hu.sztomek.archdemo.data.model.Timezone;
import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.AuthenticatedAction;
import hu.sztomek.archdemo.domain.common.PaginatedAction;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.common.UseCase;
import hu.sztomek.archdemo.domain.payloads.MapPayload;
import hu.sztomek.archdemo.presentation.common.BasePresenter;
import hu.sztomek.archdemo.presentation.common.DeleteFailedUiError;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneEvent;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import hu.sztomek.archdemo.presentation.views.AdapterItem;
import hu.sztomek.archdemo.presentation.views.LoadingItem;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class TimezonePresenter extends BasePresenter<TimezonesUiModel, Contract.View> implements Contract.Presenter {

    private final UseCase<PaginatedAction> listUseCase;
    private final UseCase<DeleteTimezoneAction> deleteUseCase;
    private final IUserManager userManager;

    public TimezonePresenter(UiState<TimezonesUiModel> latestState, UseCase<PaginatedAction> listUseCase, UseCase<DeleteTimezoneAction> deleteUseCase, IUserManager userManager) {
        super(latestState);
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
        this.userManager = userManager;
    }

    @Override
    public void attach(Contract.View view) {
        super.attach(view);
    }

    @Override
    protected ObservableTransformer<UiEvent, Action> eventsToActions() {
        return upstream -> Observable.merge(
                upstream.ofType(TimezonesUiEvents.RefreshUiEvent.class)
                        .startWith(TimezonesUiEvents.refresh())
                        .map(event -> TimezonesUiActions.refresh(userManager.getUserId(), "\"name\"", 5)),
                upstream.ofType(TimezonesUiEvents.LoadMoreUiEvent.class)
                        .map(event -> TimezonesUiActions.loadMore(userManager.getUserId(), "\"name\"", "\"" + event.getLastItem() + "\"", 5)),
                upstream.ofType(DeleteTimezoneEvent.class)
                        .map(event -> TimezonesUiActions.delete(userManager.getUserId(), event.getTimezoneId()))
        );
    }

    @Override
    protected ObservableTransformer<Action, Result> actionsToResults() {
        return upstream -> Observable.merge(
                upstream.ofType(TimezonesUiActions.RefreshAction.class)
                        .flatMap(refreshAction ->
                                listUseCase.performAction(refreshAction)
                                        .onErrorReturn(t -> Result.error(refreshAction, t))
                                        .startWith(Result.loading(refreshAction))),
                upstream.ofType(TimezonesUiActions.LoadMoreAction.class)
                        .flatMap(loadMoreAction ->
                                listUseCase.performAction(loadMoreAction)
                                        .onErrorReturn(t -> Result.error(loadMoreAction, t))
                                        .startWith(Result.loading(loadMoreAction))),
                upstream.ofType(DeleteTimezoneAction.class)
                        .flatMap(action ->
                                deleteUseCase.performAction(action)
                                        .onErrorReturn(t -> Result.error(action, t))
                                        .startWith(Result.loading(action)))
        );
    }

    @Override
    protected UiState<TimezonesUiModel> accumulate(UiState<TimezonesUiModel> current, Result latest) {
        UiState.Builder<TimezonesUiModel> builder = current.toBuilder();

        TimezonesUiModel data = current.getData();

        // don't show load in this case, errors are also handled here
        if (latest.getAction() instanceof DeleteTimezoneAction) {
            DeleteTimezoneAction action = (DeleteTimezoneAction) latest.getAction();
            if (latest.getError() != null) {
                builder.setError(new DeleteFailedUiError("Delete operation was not successful. Item restored."));
            } else {
                data.setTimezones(
                        Observable.fromIterable(data.getTimezones())
                                .filter(item -> item instanceof TimezoneModelAdapterItem && !((TimezoneModelAdapterItem) item).getData().getId().equals(action.getTimezoneId()))
                                .toList()
                                .blockingGet()
                );
            }

            builder.setData(data);
            return builder.build();
        }

        // default error handling
        if (latest.getError() != null) {
            builder.setError(new UiError(true, latest.getError().getMessage() == null ? latest.getError().getClass().getSimpleName() : latest.getError().getMessage()));
            return builder.build();
        }

        builder.setError(null);
        // default loading handling
        builder.setLoading(latest.isLoading());

        if (latest.getPayload() instanceof MapPayload) {
            MapPayload<Timezone> payload = (MapPayload) latest.getPayload();

            final String uid;
            if (latest.getAction() instanceof AuthenticatedAction) {
                uid = ((AuthenticatedAction) latest.getAction()).getUid();
            } else {
                uid = null;
            }

            Observable<AdapterItem> previousWithoutLoading = Observable.fromIterable(current.getData().getTimezones())
                    .filter(a -> a instanceof TimezoneModelAdapterItem);
            final List<AdapterItem> timezoneModels;
            if (payload.getData() != null) {
                Observable<AdapterItem> sorted = Observable.fromIterable(payload.getData().entrySet())
                        .map((Function<Map.Entry<String, Timezone>, AdapterItem>) entry -> {
                            TimezoneModel model = new TimezoneModel();
                            model.setId(entry.getKey());
                            Timezone timezone = entry.getValue();
                            model.setName(timezone.getName());
                            model.setDifference(String.valueOf(timezone.getDiff()));
                            model.setCity(timezone.getCity());
                            model.setOwnerId(uid);
                            return new TimezoneModelAdapterItem(model);
                        })
                        .mergeWith(previousWithoutLoading) // remove loading item
                        .sorted((one, two) -> one.getKey().compareTo(two.getKey()));
                data.setListReachedEnd(previousWithoutLoading.count().blockingGet() == sorted.distinct().count().blockingGet());
                timezoneModels = sorted
                        .distinct()
                        .toList()
                        .blockingGet();
            } else {
                timezoneModels = new ArrayList<>();
            }
            data.setTimezones(timezoneModels);
        } else if (latest.getAction() instanceof TimezonesUiActions.LoadMoreAction) {
            builder.setLoading(false); // loading will be handled by the list
            if (!(data.getTimezones().get(data.getTimezones().size() - 1) instanceof LoadingItem)) {
                data.getTimezones().add(new LoadingItem(null));
            }
            data.setListReachedEnd(!latest.isLoading() && latest.getPayload() == null);
        } else if (latest.getAction() instanceof TimezonesUiActions.RefreshAction) {
            data.getTimezones().clear();
        }

        builder.setData(data);
        return builder.build();
    }

}