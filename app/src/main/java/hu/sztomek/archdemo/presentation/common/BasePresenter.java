package hu.sztomek.archdemo.presentation.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.Result;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public abstract class BasePresenter<M extends UiModel, V extends IView> implements IPresenter<M, V> {

    protected abstract ObservableTransformer<UiEvent, Action> eventsToActions();
    protected abstract ObservableTransformer<Action, Result> actionsToResults();
    protected abstract UiState<M> accumulate(UiState<M> current, Result latest);

    protected CompositeDisposable disposables;
    private V view;
    private BehaviorSubject<UiState<M>> stateStream = BehaviorSubject.create();

    public BasePresenter(UiState<M> latestState) {
        setLastState(latestState);
    }

    @Override
    public void attach(V view) {
        Timber.d(getClass().getSimpleName() + ": attach ["+view+"]");
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        this.view = view;

        ConnectableObservable<UiEvent> publish = this.view.actionStream().publish();
        disposables.add(
           publish
                    .doOnNext(e -> Timber.d( getClass().getSimpleName() + ": EVENT : " + e.getClass().getSimpleName() + ": ["+ e.toString() +"]"))
                    .compose(eventsToActions())
                    .doOnNext(a -> Timber.d(getClass().getSimpleName() + " : ACTION : " + a.getClass().getSimpleName() + ": ["+ a.toString() +"]"))
                    .compose(actionsToResults())
                    .doOnNext(r -> Timber.d(getClass().getSimpleName() + " : RESULT : " + r.getClass().getSimpleName() + ": ["+r.toString()+"]"))
                    .distinctUntilChanged()
                    .scan(getLastState(), this::accumulate)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnDispose(() -> Timber.d(getClass().getSimpleName() + ": onDispose()"))
                    .subscribe(newState -> {
                        Timber.d(getClass().getSimpleName() + ": onNext, state: " + newState.getClass().getSimpleName() + ": ["+newState+"]");
                        setLastState(newState);
                    }, throwable -> {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        throwable.printStackTrace(pw);
                        String stackTrace = sw.toString();
                        Timber.d(getClass().getSimpleName() + " : onError - "+throwable.getClass().getSimpleName()+"["+ throwable.getMessage() + stackTrace +"]");
                        setLastState(getLastState().toBuilder()
                                .setError(new UiError(true, throwable.getClass().getSimpleName() + ": " + throwable.getMessage() + " " + stackTrace)).build());
                    },
                        () -> Timber.d(getClass().getSimpleName() + ": onComplete()"),
                        d -> Timber.d(getClass().getSimpleName() + ": onSubscribe()"))
        );
        publish.connect();
    }

    @Override
    public void detach() {
        Timber.d(getClass().getSimpleName() + ": detach()");
        disposables.dispose();
        view = null;
    }

    @Override
    public UiState<M> getLastState() {
        return stateStream.getValue();
    }

    @Override
    public Observable<UiState<M>> stateStream() {
        return stateStream;
    }

    protected void setLastState(UiState<M> state) {
        stateStream.onNext(state);
    }
}
