package hu.sztomek.archdemo.presentation.common;

import io.reactivex.Observable;

public interface IView {

    Observable<UiEvent> actionStream();

}
