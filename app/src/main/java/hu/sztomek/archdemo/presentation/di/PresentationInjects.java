package hu.sztomek.archdemo.presentation.di;

import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserActivity;
import hu.sztomek.archdemo.presentation.screens.email_login.EmailLoginActivity;
import hu.sztomek.archdemo.presentation.screens.email_register.EmailRegisterActivity;
import hu.sztomek.archdemo.presentation.screens.forgot_pw.ForgotPwActivity;
import hu.sztomek.archdemo.presentation.screens.landing.LandingActivity;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditActivity;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.list.TimezoneListFragment;

public interface PresentationInjects {

    void inject(CheckUserActivity activity);
    void inject(EmailLoginActivity activity);
    void inject(EmailRegisterActivity activity);
    void inject(ForgotPwActivity activity);
    void inject(TimezoneEditActivity activity);
    void inject(TimezoneListFragment activity);
    void inject(LandingActivity activity);

}
