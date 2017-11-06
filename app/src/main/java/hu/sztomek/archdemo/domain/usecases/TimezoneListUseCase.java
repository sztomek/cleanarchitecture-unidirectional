package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.PaginatedAction;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.MapPayload;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TimezoneListUseCase extends BaseUseCase<PaginatedAction> {

    private final RestService restService;
    private final IAuthService authService;

    public TimezoneListUseCase(Scheduler workload, Scheduler delivery, RestService restService, IAuthService authService) {
        super(workload, delivery);
        this.restService = restService;
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(PaginatedAction action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s -> {
                        return restService.getTimezonesForUser(action.getUid(), s, action.getOrderBy(), action.getPageSize(), action.getLastKey())
                            .map(stringTimezoneMap -> Result.success(action, new MapPayload<>(stringTimezoneMap)));
                     }
                )
                .onErrorReturn(t -> Result.error(action, t));
    }
}
