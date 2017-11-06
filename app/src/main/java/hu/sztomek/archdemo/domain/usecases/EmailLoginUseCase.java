package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.email_login.EmailLoginActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class EmailLoginUseCase extends BaseUseCase<EmailLoginActions.LoginAction> {

    private final IAuthService authService;

    public EmailLoginUseCase(Scheduler workload, Scheduler delivery, IAuthService authService) {
        super(workload, delivery);
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(EmailLoginActions.LoginAction action) {
        return authService.signInWithEmailAndPassword(action.getEmail(), action.getPassword())
                .toSingle(() -> Result.success(action, new SuccessPayload(true))).toObservable();
    }
}
