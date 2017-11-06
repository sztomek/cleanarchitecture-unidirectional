package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.email_register.EmailRegisterActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class EmailRegisterUseCase extends BaseUseCase<EmailRegisterActions.EmailRegister> {

    private final IAuthService authService;

    public EmailRegisterUseCase(Scheduler workload, Scheduler delivery, IAuthService authService) {
        super(workload, delivery);
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(EmailRegisterActions.EmailRegister action) {
        return authService.registerWithEmailAndPassword(action.getEmail(), action.getPassword())
                .toSingle(() -> Result.success(action, new SuccessPayload(true))).toObservable();
    }
}
