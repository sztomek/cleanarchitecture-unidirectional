package hu.sztomek.archdemo.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public abstract class BaseFragment<M extends UiModel> extends Fragment implements IView {

    protected final PublishSubject<UiEvent> events = PublishSubject.create();
    private CompositeDisposable disposables;

    protected abstract void createPresenter(UiState<M> initialState);
    protected abstract void saveStateToBundle(Bundle persistence);
    protected abstract UiState<M> restoreStateFromBundle(Bundle persistence);
    protected abstract View inflateView(LayoutInflater inflater, ViewGroup container);
    protected abstract IPresenter getPresenter();
    protected abstract void render(UiState<M> state);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflateView(inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getPresenter() == null) {
            Timber.d(getClass().getSimpleName() + ": onViewCreated -- createPresenter");
            createPresenter(restoreStateFromBundle(savedInstanceState));
        }

        getPresenter().attach(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d(getClass().getSimpleName() + ": onSaveInstanceState()");
        saveStateToBundle(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d(getClass().getSimpleName() + ": onStart()");
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        final IPresenter<M, IView> presenter = getPresenter();
        disposables.add(
                presenter.stateStream()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::render)
        );
    }

    @Override
    public void onStop() {
        Timber.d(getClass().getSimpleName() + ": onStop()");
        disposables.dispose();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Timber.d(getClass().getSimpleName() + ": onDestroy()");
        getPresenter().detach();
        super.onDestroy();
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return events;
    }
}