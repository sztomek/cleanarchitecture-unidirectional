package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TimezoneDeleteUseCase extends BaseUseCase<DeleteTimezoneAction> {

    private final RestService restService;
    private final IAuthService authService;

    public TimezoneDeleteUseCase(Scheduler workload, Scheduler delivery, RestService restService, IAuthService authService) {
        super(workload, delivery);
        this.restService = restService;
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(DeleteTimezoneAction action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s ->
                    restService.deleteTimezone(action.getUid(), action.getTimezoneId(), s)
                        .map(baseResponse -> Result.success(action, new SuccessPayload(true)))
                )
                .onErrorReturn(t -> Result.error(action, t));
    }
}
