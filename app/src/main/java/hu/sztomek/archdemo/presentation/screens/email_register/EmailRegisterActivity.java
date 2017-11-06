package hu.sztomek.archdemo.presentation.screens.email_register;

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

public class EmailRegisterActivity extends BaseActivity<EmailRegisterUiModel> implements Contract.View {

    public static Intent getStarter(Context context, String email) {
        final Intent intent = new Intent(context, EmailRegisterActivity.class);
        intent.putExtra(Constants.EXTRA_EMAIL, email);
        return intent;
    }

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;
    @BindView(R.id.btnRegister)
    View btnRegister;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;

    @Inject
    EmailRegisterPresenter presenter;
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
                .presentationModule(new PresentationModule(null, ((EmailRegisterPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<EmailRegisterUiModel> initialState) {
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
    protected UiState<EmailRegisterUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.idle();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return RxView.clicks(btnRegister)
                .map(o -> EmailRegisterUiEvents.register(etEmail.getText().toString(), etPassword.getText().toString(), etPasswordConfirm.getText().toString()));
    }

    @Override
    public void render(@Nonnull UiState<EmailRegisterUiModel> state) {
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
                    Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show();
                    final Intent data = new Intent();
                    data.putExtra(Constants.EXTRA_EMAIL, etEmail.getText().toString());
                    setResult(RESULT_OK, data);
                    router.close();
                }
            }
        }
    }

    private void setUiEnabled(boolean enabled) {
        etEmail.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        etPasswordConfirm.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }
}
