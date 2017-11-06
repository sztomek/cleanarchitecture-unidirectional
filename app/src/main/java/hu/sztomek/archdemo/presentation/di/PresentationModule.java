package hu.sztomek.archdemo.presentation.di;

import javax.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import hu.sztomek.archdemo.common.PerScreen;
import hu.sztomek.archdemo.data.datasource.IUserManager;
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
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserPresenter;
import hu.sztomek.archdemo.presentation.screens.email_login.EmailLoginPresenter;
import hu.sztomek.archdemo.presentation.screens.email_register.EmailRegisterPresenter;
import hu.sztomek.archdemo.presentation.screens.forgot_pw.ForgotPwPresenter;
import hu.sztomek.archdemo.presentation.screens.landing.LandingPresenter;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditPresenter;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.list.TimezonePresenter;
import hu.sztomek.archdemo.presentation.screens.login.LoginPresenter;

@Module
public class PresentationModule {

    private final @Nullable UiState lastState;
    private final @Nullable IPresenter presenter;

    public PresentationModule(@Nullable UiState lastState, @Nullable IPresenter presenter) {
        this.lastState = lastState;
        this.presenter = presenter;
    }

    @Nullable
    @Provides
    UiState provideLastState() {
        return lastState;
    }

    @Nullable
    @Provides
    IPresenter providePresenter() {
        return presenter;
    }

    @PerScreen
    @Provides
    CheckUserPresenter provideCheckUserPresenter(@Nullable UiState lastState, UserExistsUseCase checkUserUseCase, RegisterUserUseCase registerUserUseCase, IUserManager userManager, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((CheckUserPresenter) presenter);
        }
        return new CheckUserPresenter(lastState, checkUserUseCase, registerUserUseCase, userManager);
    }

    @PerScreen
    @Provides
    EmailLoginPresenter provideEmailLoginPresenter(@Nullable UiState lastState, EmailLoginUseCase emailLoginU, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((EmailLoginPresenter) presenter);
        }
        return new EmailLoginPresenter(lastState, emailLoginU);
    }

    @PerScreen
    @Provides
    EmailRegisterPresenter provideEmailRegisterPresenter(@Nullable UiState lastState, EmailRegisterUseCase emailRegisterUseCase, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((EmailRegisterPresenter) presenter);
        }
        return new EmailRegisterPresenter(lastState, emailRegisterUseCase);
    }

    @PerScreen
    @Provides
    ForgotPwPresenter provideForgotPwPresenter(@Nullable UiState lastState, ForgotPwUseCase forgotPwUseCase, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((ForgotPwPresenter) presenter);
        }
        return new ForgotPwPresenter(lastState, forgotPwUseCase);
    }

    @PerScreen
    @Provides
    LoginPresenter provideLoginPresenter(@Nullable UiState lastState, GoogleLoginUseCase useCase, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((LoginPresenter) presenter);
        }
        return new LoginPresenter(lastState, useCase);
    }

    @PerScreen
    @Provides
    LandingPresenter provideLandingPresenter(@Nullable UiState lastState, LogoutUseCase logoutUseCase, GetUserUseCase getUserUseCase, IUserManager userManager, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((LandingPresenter) presenter);
        }
        return new LandingPresenter(lastState, logoutUseCase, getUserUseCase, userManager);
    }

    @PerScreen
    @Provides
    TimezonePresenter provideTimezonePresenter(@Nullable UiState lastState, TimezoneListUseCase listUseCase, TimezoneDeleteUseCase deleteUseCase, IUserManager userManager, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((TimezonePresenter) presenter);
        }
        return new TimezonePresenter(lastState, listUseCase, deleteUseCase, userManager);
    }

    @PerScreen
    @Provides
    TimezoneEditPresenter provideTimezoneEditPresenter(@Nullable UiState lastState, TimezoneGetUseCase getUseCase, TimezoneSaveUseCase saveUseCase, TimezoneUpdateUseCase updateUseCase, TimezoneDeleteUseCase deleteUseCase, @Nullable IPresenter presenter) {
        if (presenter != null) {
            return ((TimezoneEditPresenter) presenter);
        }
        return new TimezoneEditPresenter(lastState, getUseCase, saveUseCase, updateUseCase, deleteUseCase);
    }

}
