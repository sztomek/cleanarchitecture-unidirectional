package hu.sztomek.archdemo.presentation.screens.landing;

import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.IView;

final class Contract {

    public interface View extends IView {}

    public interface Presenter extends IPresenter<LandingUiModel, View> {}

}
