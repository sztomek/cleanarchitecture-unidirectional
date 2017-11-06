package hu.sztomek.archdemo.presentation.screens.login;

import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.IView;

final class Contract {

    private Contract() {}

    public interface View extends IView {}

    public interface Presenter extends IPresenter<LoginUiModel, View> {}

}
