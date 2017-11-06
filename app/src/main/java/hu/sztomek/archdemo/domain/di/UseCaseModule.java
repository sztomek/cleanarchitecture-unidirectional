package hu.sztomek.archdemo.domain.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.sztomek.archdemo.common.ThreadModule;
import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.usecases.EmailLoginUseCase;
import hu.sztomek.archdemo.domain.usecases.EmailRegisterUseCase;
import hu.sztomek.archdemo.domain.usecases.ForgotPwUseCase;
import hu.sztomek.archdemo.domain.usecases.GetUserUseCase;
import hu.sztomek.archdemo.domain.usecases.GoogleLoginUseCase;
import hu.sztomek.archdemo.domain.usecases.LogoutUseCase;
import hu.sztomek.archdemo.domain.usecases.RegisterUserUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneDeleteUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneGetUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneListUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneSaveUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneUpdateUseCase;
import hu.sztomek.archdemo.domain.usecases.UserExistsUseCase;
import io.reactivex.Scheduler;

@Module
public class UseCaseModule {

    @Provides
    @Singleton
    EmailLoginUseCase provideEmailLogin(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService) {
        return new EmailLoginUseCase(workload, delivery, authService);
    }

    @Provides
    @Singleton
    EmailRegisterUseCase provideEmailRegister(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService) {
        return new EmailRegisterUseCase(workload, delivery, authService);
    }

    @Provides
    @Singleton
    ForgotPwUseCase provideForgotPw(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService) {
        return new ForgotPwUseCase(workload, delivery, authService);
    }

    @Provides
    @Singleton
    GetUserUseCase provideGetUser(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new GetUserUseCase(workload, delivery, restService, authService);
    }

    @Provides
    @Singleton
    GoogleLoginUseCase provideGoogleLogin(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService) {
        return new GoogleLoginUseCase(workload, delivery, authService);
    }

    @Provides
    @Singleton
    LogoutUseCase provideLogout(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService) {
        return new LogoutUseCase(workload, delivery, authService);
    }

    @Provides
    @Singleton
    RegisterUserUseCase provideUserRegister(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new RegisterUserUseCase(workload, delivery, authService, restService);
    }

    @Provides
    @Singleton
    TimezoneDeleteUseCase provideTzDelete(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new TimezoneDeleteUseCase(workload, delivery, restService, authService);
    }

    @Provides
    @Singleton
    TimezoneGetUseCase provideTzGet(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new TimezoneGetUseCase(workload, delivery, restService, authService);
    }

    @Provides
    @Singleton
    TimezoneListUseCase provideTzList(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new TimezoneListUseCase(workload, delivery, restService, authService);
    }


    @Provides
    @Singleton
    TimezoneSaveUseCase provideTzSave(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new TimezoneSaveUseCase(workload, delivery, restService, authService);
    }


    @Provides
    @Singleton
    TimezoneUpdateUseCase provideTzUpdate(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new TimezoneUpdateUseCase(workload, delivery, restService, authService);
    }


    @Provides
    @Singleton
    UserExistsUseCase provideUserExists(@Named(ThreadModule.NAMED_WORKER) Scheduler workload, @Named(ThreadModule.NAMED_DELIVERY) Scheduler delivery, IAuthService authService, RestService restService) {
        return new UserExistsUseCase(workload, delivery, authService, restService);
    }

    public interface Exposes {

        EmailLoginUseCase emailLogin();
        EmailRegisterUseCase emailRegister();
        ForgotPwUseCase forgotPw();
        GetUserUseCase getUser();
        GoogleLoginUseCase googleLogin();
        LogoutUseCase logout();
        RegisterUserUseCase userRegister();
        TimezoneDeleteUseCase timezoneDelete();
        TimezoneGetUseCase timezoneGet();
        TimezoneListUseCase timezoneList();
        TimezoneSaveUseCase timezoneSave();
        TimezoneUpdateUseCase timezoneUpdate();
        UserExistsUseCase userExists();

    }

}
