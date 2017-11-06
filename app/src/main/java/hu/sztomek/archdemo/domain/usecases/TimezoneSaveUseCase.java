package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.data.model.Timezone;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditUiActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TimezoneSaveUseCase extends BaseUseCase<TimezoneEditUiActions.SaveTimezoneAction> {

    private final RestService restService;
    private final IAuthService authService;

    public TimezoneSaveUseCase(Scheduler workload, Scheduler delivery, RestService restService, IAuthService authService) {
        super(workload, delivery);
        this.restService = restService;
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(TimezoneEditUiActions.SaveTimezoneAction action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s -> {
                    final Timezone timezone = new Timezone();
                    timezone.setCity(action.getCity());
                    timezone.setDiff(action.getDifference());
                    timezone.setName(action.getName());
                    return restService.saveTimezone(action.getUid(), timezone, s)
                            .map(postResponse -> Result.success(action, new SuccessPayload(true)));
                })
                .onErrorReturn(t -> Result.error(action, t));
    }
}
