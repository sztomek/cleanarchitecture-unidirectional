package hu.sztomek.archdemo.presentation.screens.email_login;

import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.IView;

public final class Contract {

    private Contract() {}

    public interface Presenter extends IPresenter<EmailLoginUiModel, View> {

    }

    public interface View extends IView {

    }

}
