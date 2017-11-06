package hu.sztomek.archdemo.presentation.screens.check_user;

import android.os.Bundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseActivity;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.views.LoadingRetryView;

public class CheckUserActivity extends BaseActivity<CheckUserUiModel> implements Contract.View {

    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;
    @Inject
    CheckUserPresenter presenter;
    @Inject
    IRouter router;

    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onGetLastCustomNonConfigurationInstance(Object instance) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(null, ((CheckUserPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<CheckUserUiModel> initialState) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(initialState, null))
                .build()
                .inject(this);
    }

    @Override
    protected void saveStateToBundle(Bundle persistence) {

    }

    @Override
    protected UiState<CheckUserUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.idle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        ButterKnife.bind(this);
        loadingRetryView.setState(LoadingRetryView.STATE_LOADING);
        loadingRetryView.setRetryListener(() -> events.onNext(new CheckUserUiEvents.RetryCheckUserUiEvent()));
    }

    @Override
    public void render(@Nonnull UiState<CheckUserUiModel> state) {
        if (state.isLoading()) {
            loadingRetryView.setState(LoadingRetryView.STATE_LOADING);
        } else if (state.getError() != null) {
            loadingRetryView.setState(LoadingRetryView.STATE_RETRY);
            loadingRetryView.setRetryMessage(state.getError().getMessage());
        }

        if (state.getData() != null) {
            loadingRetryView.setLoadingMessage(state.getData().getLoadingMessage());
            if (state.getData().canProceed()) {
                router.toLanding();
                router.close();
            }
            if (state.getData().getPhase() == CheckPhases.STATE_DOESNT_EXISTS) {
                events.onNext(new CheckUserUiEvents.CheckUserUiEvent());
            }
        }
    }
}
