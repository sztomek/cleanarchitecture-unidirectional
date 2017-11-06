package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.landing.LandingUiActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class LogoutUseCase extends BaseUseCase<LandingUiActions.LogoutAction> {

    private final IAuthService authService;

    public LogoutUseCase(Scheduler workload, Scheduler delivery, IAuthService authService) {
        super(workload, delivery);
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(LandingUiActions.LogoutAction action) {
        return authService.signOut()
                .toSingle(() -> Result.success(action, new SuccessPayload(true)))
                .toObservable();
    }
}
