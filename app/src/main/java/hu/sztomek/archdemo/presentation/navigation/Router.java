package hu.sztomek.archdemo.presentation.navigation;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.common.PerScreen;
import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserActivity;
import hu.sztomek.archdemo.presentation.screens.email_login.EmailLoginActivity;
import hu.sztomek.archdemo.presentation.screens.email_register.EmailRegisterActivity;
import hu.sztomek.archdemo.presentation.screens.forgot_pw.ForgotPwActivity;
import hu.sztomek.archdemo.presentation.screens.landing.LandingActivity;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditActivity;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.list.TimezoneListFragment;
import hu.sztomek.archdemo.presentation.screens.login.LoginActivity;
import hu.sztomek.archdemo.presentation.screens.splash.SplashActivity;

@PerScreen
public class Router implements IRouter {

    private static final String TAG_CURRENT_FRAGMENT = "currentFragment";

    private final FragmentActivity activity;

    public Router(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void restart() {
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        close();
    }

    @Override
    public void close() {
        activity.finish();
    }

    @Override
    public void back() {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStack();
        } else {
            close();
        }

    }

    @Override
    public void toEmailLogin() {
        activity.startActivity(new Intent(activity, EmailLoginActivity.class));
    }

    @Override
    public void toEmailRegister(String email, int req) {
        activity.startActivityForResult(EmailRegisterActivity.getStarter(activity, email), req);
    }

    @Override
    public void toForgotPassword(String email) {
        activity.startActivity(ForgotPwActivity.getStarter(activity, email));
    }

    @Override
    public void toCheckUser() {
        activity.startActivity(new Intent(activity, CheckUserActivity.class));
    }

    @Override
    public void toLanding() {
        activity.startActivity(new Intent(activity, LandingActivity.class));
    }

    @Override
    public void toLogin() {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void showTimezoneList() {
        changeContent(new TimezoneListFragment(), false);
    }

    private void changeContent(Fragment newContent, boolean addToBackStack) {
        Fragment fragmentByTag = activity.getSupportFragmentManager().findFragmentByTag(TAG_CURRENT_FRAGMENT);
        if (fragmentByTag == null || !fragmentByTag.getClass().equals(newContent.getClass())) {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, newContent, TAG_CURRENT_FRAGMENT);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(newContent.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void toCreateTimezone(String userId) {
        activity.startActivity(TimezoneEditActivity.forCreate(activity, userId));
    }

    @Override
    public void toEditTimezone(String userId, String tzid) {
        activity.startActivity(TimezoneEditActivity.forEdit(activity, userId, tzid));
    }
}
