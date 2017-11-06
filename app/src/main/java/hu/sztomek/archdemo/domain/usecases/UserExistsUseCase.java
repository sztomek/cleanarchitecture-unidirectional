package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class UserExistsUseCase extends BaseUseCase<CheckUserActions.CheckUserExists> {

    private final IAuthService authService;
    private final RestService restService;

    public UserExistsUseCase(Scheduler workload, Scheduler delivery, IAuthService authService, RestService restService) {
        super(workload, delivery);
        this.authService = authService;
        this.restService = restService;
    }

    @Override
    protected Observable<Result> doAction(CheckUserActions.CheckUserExists action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s ->
                    restService.getUser(action.getUid(), s)
                        .map(profile -> Result.success(action, new SuccessPayload(true)))
                )
                .onErrorReturn(t -> Result.error(action, t));
    }
}
