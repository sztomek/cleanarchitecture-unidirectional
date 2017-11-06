package hu.sztomek.archdemo.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public abstract class BaseActivity<M extends UiModel> extends AppCompatActivity implements IView {

    protected final PublishSubject<UiEvent> events = PublishSubject.create();
    private CompositeDisposable disposables;

    protected abstract void createPresenter(UiState<M> initialState);
    protected abstract void saveStateToBundle(Bundle persistence);
    protected abstract UiState<M> restoreStateFromBundle(Bundle persistence);
    protected abstract IPresenter getPresenter();
    protected abstract void onGetLastCustomNonConfigurationInstance(Object instance);
    protected abstract void render(UiState<M> state);
    private boolean stateSaved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d(getClass().getSimpleName() + ": onCreate["+savedInstanceState+"]");
        if (getLastCustomNonConfigurationInstance() instanceof IPresenter) {
            Timber.d(getClass().getSimpleName() + ": onCreate -- getLastCustomNonConfigurationInstance()");
            onGetLastCustomNonConfigurationInstance(getLastCustomNonConfigurationInstance());
        } else {
            Timber.d(getClass().getSimpleName() + ": onCreate -- createPresenter");
            createPresenter(restoreStateFromBundle(savedInstanceState));
        }

        stateSaved = false;

        getPresenter().attach(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d(getClass().getSimpleName() + ": onSaveInstanceState()");
        saveStateToBundle(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Timber.d(getClass().getSimpleName() + ": onRetainCustomNonConfigurationInstance()");

        stateSaved = true;

        return getPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d(getClass().getSimpleName() + ": onStart()");
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        final IPresenter<M, IView> presenter = getPresenter();
        disposables.add(
                presenter.stateStream()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(state -> Timber.d(getClass().getSimpleName() + ": render [" + state + "]"))
                    .subscribe(this::render)
        );
    }

    @Override
    protected void onStop() {
        disposables.dispose();
        Timber.d(getClass().getSimpleName() + ": onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Timber.d(getClass().getSimpleName() + ": onDestroy()");
        if (!stateSaved) {
            getPresenter().detach();
        }
        super.onDestroy();
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return events;
    }
}
