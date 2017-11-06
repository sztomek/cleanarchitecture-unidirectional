package hu.sztomek.archdemo.presentation.screens.email_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class EmailLoginActivity extends BaseActivity<EmailLoginUiModel> {

    private static final int RQ_REGISTER = 1234;

    @BindView(R.id.btnLogin)
    View btnLogin;
    @BindView(R.id.btnRegister)
    View btnRegister;
    @BindView(R.id.twForgotPw)
    View tvForgotPw;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;

    @Inject
    EmailLoginPresenter presenter;
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
                .presentationModule(new PresentationModule(null, ((EmailLoginPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<EmailLoginUiModel> initialState) {
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
    protected UiState<EmailLoginUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.idle();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        ButterKnife.bind(this);
    }

    private Observable<UiEvent> loginClick() {
        return RxView.clicks(btnLogin)
                .map(aObject -> EmailLoginUiEvents.login(etEmail.getText().toString(), etPassword.getText().toString()));
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return loginClick();
    }

    @Override
    public void render(@NonNull UiState<EmailLoginUiModel> state) {
        if (state.isLoading()) {
            tvError.setText("");
            tvError.setVisibility(View.INVISIBLE);
            setUiEnabled(false);
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
                    router.toCheckUser();
                }
            }
        }
    }

    private void setUiEnabled(boolean enabled) {
        etEmail.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        btnLogin.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
        tvForgotPw.setEnabled(enabled);
    }

    @OnClick(R.id.twForgotPw)
    public void onClickForgotPw(View view) {
        router.toForgotPassword(etEmail.getText().toString());
    }

    @OnClick(R.id.btnRegister)
    public void onClickRegister(View view) {
        router.toEmailRegister(etEmail.getText().toString(), RQ_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_REGISTER && resultCode == RESULT_OK) {
            etEmail.setText(data.getStringExtra(Constants.EXTRA_EMAIL));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
