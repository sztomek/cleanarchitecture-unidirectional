package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.forgot_pw.ForgotPwActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class ForgotPwUseCase extends BaseUseCase<ForgotPwActions.ForgotPwSubmit> {

    private final IAuthService authService;

    public ForgotPwUseCase(Scheduler workload, Scheduler delivery, IAuthService authService) {
        super(workload, delivery);
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(ForgotPwActions.ForgotPwSubmit action) {
        return authService.sendPasswordReset(action.getEmail())
                .toSingle(() -> Result.success(action, new SuccessPayload(true))).toObservable();
    }
}
