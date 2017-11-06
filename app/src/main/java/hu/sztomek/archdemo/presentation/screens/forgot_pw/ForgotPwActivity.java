package hu.sztomek.archdemo.presentation.screens.forgot_pw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.Constants;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseActivity;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.views.LoadingRetryView;
import io.reactivex.Observable;

public class ForgotPwActivity extends BaseActivity<ForgotPwUiModel> implements Contract.View {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnSubmit)
    View btnSubmit;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;

    @Inject
    ForgotPwPresenter presenter;
    @Inject
    IRouter router;

    public static Intent getStarter(Context context, String email) {
        final Intent intent = new Intent(context, ForgotPwActivity.class);
        intent.putExtra(Constants.EXTRA_EMAIL, email);
        return intent;
    }

    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onGetLastCustomNonConfigurationInstance(Object instance) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(null, ((ForgotPwPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<ForgotPwUiModel> initialState) {
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
    protected UiState<ForgotPwUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.idle();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(Constants.EXTRA_EMAIL)) {
            etEmail.setText(getIntent().getStringExtra(Constants.EXTRA_EMAIL));
        }
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return RxView.clicks(btnSubmit)
                .map(o -> ForgotPwUiEvents.submit(etEmail.getText().toString()));
    }

    @Override
    public void render(@Nonnull UiState<ForgotPwUiModel> state) {
        if (state.isLoading()) {
            setUiEnabled(false);
            tvError.setText("");
            tvError.setVisibility(View.INVISIBLE);
            loadingRetryView.setState(LoadingRetryView.STATE_LOADING);
        } else {
            loadingRetryView.setState(LoadingRetryView.STATE_IDLE);
            setUiEnabled(true);
            if (state.getError() != null) {
                tvError.setText(state.getError().getMessage());
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setText("");
                tvError.setVisibility(View.INVISIBLE);

                if (state.getData() != null && state.getData().isSuccess()) {
                    Toast.makeText(this, R.string.reset_sent, Toast.LENGTH_SHORT).show();
                    router.close();
                }
            }
        }
    }

    private void setUiEnabled(boolean enabled) {
        etEmail.setEnabled(enabled);
        btnSubmit.setEnabled(enabled);
    }
}
