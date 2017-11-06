package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.data.datasource.IUserManager;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseFragment;
import hu.sztomek.archdemo.presentation.common.DeleteFailedUiError;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.views.ItemClickListener;
import hu.sztomek.archdemo.presentation.views.LoadingRetryView;
import hu.sztomek.archdemo.presentation.views.SwipeRefreshWithText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class TimezoneListFragment extends BaseFragment<TimezonesUiModel> implements Contract.View {

    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;
    @BindView(R.id.fabAdd)
    FloatingActionButton fab;
    @BindView(R.id.srwt)
    SwipeRefreshWithText swipeRefreshWithText;
    @BindView(R.id.etSearch)
    EditText etSearch;
    private TimezoneAdapter adapter;

    @Inject TimezonePresenter presenter;
    @Inject IUserManager userManager;
    @Inject IRouter router;

    private CompositeDisposable disposables;

    public class RefreshRunnable implements Runnable {
        @Override
        public void run() {
            if (isVisible()) {
                adapter.notifyDataSetChanged();
                swipeRefreshWithText.getRecyclerView().postDelayed(new RefreshRunnable(), 60 * 1000L);
            }
        }
    }

    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void createPresenter(UiState<TimezonesUiModel> initialState) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(getActivity()))
                .presentationModule(new PresentationModule(initialState, null))
                .build()
                .inject(this);
    }

    @Override
    protected void saveStateToBundle(Bundle persistence) {

    }

    @Override
    protected UiState<TimezonesUiModel> restoreStateFromBundle(Bundle persistence) {
        TimezonesUiModel data = new TimezonesUiModel();
        data.setTimezones(new ArrayList<>());
        return UiState.<TimezonesUiModel>idle().toBuilder().setData(data).build();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_timezones, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingRetryView.setRetryListener(() -> events.onNext(TimezonesUiEvents.refresh()));
        swipeRefreshWithText.getSwipeRefreshLayout().setOnRefreshListener(() -> events.onNext(TimezonesUiEvents.refresh()));
        swipeRefreshWithText.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshWithText.getRecyclerView().post(new RefreshRunnable());
        adapter = new TimezoneAdapter(new ItemClickListener() {
            @Override
            public void onDeleteClicked(int item) {
                events.onNext(TimezonesUiEvents.delete(userManager.getUserId(), ((TimezoneModelAdapterItem) adapter.getItem(item)).getData().getId()));
                adapter.delete(item); // remove item from adapter, presenter still maintains the original list - will be useful if delete operation fails and item needs to be restored.
            }

            @Override
            public void onItemClicked(int item) {
                router.toEditTimezone(userManager.getUserId(), ((TimezoneModelAdapterItem) adapter.getItem(item)).getData().getId());
            }
        });

        adapter.setLoadListener(() -> events.onNext(TimezonesUiEvents.loadMore(adapter.getLastItemId())));
        swipeRefreshWithText.getRecyclerView().setAdapter(adapter);
        swipeRefreshWithText.getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        fab.setOnClickListener(v -> router.toCreateTimezone(userManager.getUserId()));

        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        disposables.add(
            RxTextView.afterTextChangeEvents(etSearch)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    if (e.editable() != null) {
                        adapter.getFilter().filter(e.editable().toString());
                    }})
        );
    }

    @Override
    public void onDestroyView() {
        disposables.dispose();
        super.onDestroyView();
    }

    @Override
    public void render(@Nonnull UiState<TimezonesUiModel> state) {
        loadingRetryView.setState(state.isLoading() ? LoadingRetryView.STATE_LOADING : state.getError() == null ? LoadingRetryView.STATE_IDLE : LoadingRetryView.STATE_RETRY);
        if (state.getError() != null) {
            if (state.getError() instanceof DeleteFailedUiError) {
                Toast.makeText(getActivity(), state.getError().getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                loadingRetryView.setRetryMessage(state.getError().getMessage());
            }
        }

        swipeRefreshWithText.getSwipeRefreshLayout().setRefreshing(state.isLoading() && state.getData().getTimezones().isEmpty());

        if (state.getData() != null) {
            adapter.setEnableAutoLoad(!state.getData().isListReachedEnd());
            adapter.setData(state.getData().getTimezones());
            swipeRefreshWithText.setShowText(state.getData().getTimezones().isEmpty());
        }
    }
}
