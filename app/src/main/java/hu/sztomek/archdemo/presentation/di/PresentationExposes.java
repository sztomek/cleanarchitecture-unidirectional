package hu.sztomek.archdemo.presentation.di;

import android.support.v4.app.FragmentActivity;

import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.screens.login.LoginPresenter;

public interface PresentationExposes {

    FragmentActivity getActivity();
    IRouter getRouter();
    LoginPresenter getPresenter();

}
