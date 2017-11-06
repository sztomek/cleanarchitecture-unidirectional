package hu.sztomek.archdemo.presentation.common;

import io.reactivex.Observable;

public interface IPresenter<M extends UiModel, V extends IView> {

    void attach(V view);
    void detach();
    UiState<M> getLastState();
    Observable<UiState<M>> stateStream();

}
