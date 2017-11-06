package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.TimezonePayload;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditUiActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TimezoneGetUseCase extends BaseUseCase<TimezoneEditUiActions.GetTimezoneAction> {

    private final RestService restService;
    private final IAuthService authService;

    public TimezoneGetUseCase(Scheduler workload, Scheduler delivery, RestService restService, IAuthService authService) {
        super(workload, delivery);
        this.restService = restService;
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(TimezoneEditUiActions.GetTimezoneAction action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s ->
                        restService.getTimezoneForUser(action.getUid(), action.getTimezoneId(), s)
                                .map(timezone -> {
                                    final TimezonePayload payload = new TimezonePayload();
                                    payload.setCity(timezone.getCity());
                                    payload.setName(timezone.getName());
                                    payload.setDifference(timezone.getDiff());
                                    return Result.success(action, payload);
                                })

                )
                .onErrorReturn(t -> Result.error(action, t));
    }
}
