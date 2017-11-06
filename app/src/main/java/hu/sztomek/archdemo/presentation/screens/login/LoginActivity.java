package hu.sztomek.archdemo.presentation.screens.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseActivity;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.views.LoadingRetryView;

public class LoginActivity extends BaseActivity<LoginUiModel> implements Contract.View, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_SIGNIN = 4000;

    @BindView(R.id.btnEmail)
    View btnEmail;
    @BindView(R.id.btnFacebook)
    View btnFacebook;
    @BindView(R.id.btnGoogle)
    View btnGoogle;
    @BindView(R.id.btnTwitter)
    View btnTwitter;
    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;

    @Inject
    GoogleApiClient apiClient;
    @Inject
    LoginPresenter presenter;
    @Inject
    IRouter router;


    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onGetLastCustomNonConfigurationInstance(Object instance) {
        DaggerPresentationComponent.Builder builder = DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .presentationModule(new PresentationModule(null, ((LoginPresenter) instance)))
                .routerModule(new RouterModule(this));
        PresentationComponent presComponent = builder.build();
        DaggerLoginComponent.builder()
                .presentationComponent(presComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<LoginUiModel> initialState) {
        DaggerPresentationComponent.Builder builder = DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(initialState, null));
        PresentationComponent presComponent = builder.build();
        DaggerLoginComponent.builder()
                .presentationComponent(presComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void saveStateToBundle(Bundle persistence) {

    }

    @Override
    protected UiState<LoginUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.idle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnEmail, R.id.btnFacebook, R.id.btnTwitter, R.id.btnGoogle})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEmail: {
                router.toEmailLogin();
                break;
            }
            case R.id.btnFacebook: {
                // TODO fb login
                break;
            }
            case R.id.btnGoogle: {
                final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGNIN);

                break;
            }
            case R.id.btnTwitter: {
                // TODO twitter login
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to initialize GoogleApiClient!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_GOOGLE_SIGNIN: {
                final GoogleSignInResult signInResultFromIntent = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (signInResultFromIntent.isSuccess() && signInResultFromIntent.getSignInAccount() != null) {
                    events.onNext(LoginUiEvents.google(signInResultFromIntent.getSignInAccount().getIdToken()));
                } else {
                    Toast.makeText(this, "Google signin failed!", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            default: {
                break;
            }
        }

    }

    @Override
    public void render(@Nonnull UiState<LoginUiModel> state) {
        if (state.isLoading()) {
            loadingRetryView.setState(LoadingRetryView.STATE_LOADING);
        } else {
            loadingRetryView.setState(LoadingRetryView.STATE_IDLE);
            if (state.getError() != null) {
                Toast.makeText(this, state.getError().getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (state.getData() != null && state.getData().isSuccess()) {
                    Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                    router.toCheckUser();
                    router.close();
                }
            }
        }
    }
}
