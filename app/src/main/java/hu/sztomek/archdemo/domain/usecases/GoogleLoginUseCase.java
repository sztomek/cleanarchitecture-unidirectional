package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.login.LoginActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GoogleLoginUseCase extends BaseUseCase<LoginActions.GoogleLoginAction> {

    private final IAuthService authService;

    public GoogleLoginUseCase(Scheduler workload, Scheduler delivery, IAuthService authService) {
        super(workload, delivery);
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(LoginActions.GoogleLoginAction action) {
        return authService.signInWithGoogle(action.getToken())
                .toSingle(() -> Result.success(action, new SuccessPayload(true))).toObservable();
    }

}
