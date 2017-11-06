package hu.sztomek.archdemo.presentation.screens.login;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import hu.sztomek.archdemo.R;

@ForLogin
@Module
public class LoginModule {

    private final GoogleApiClient.OnConnectionFailedListener listener;

    public LoginModule(GoogleApiClient.OnConnectionFailedListener listener) {
        this.listener = listener;
    }

    @ForLogin
    @Provides
    GoogleApiClient provideGoogleApiClient(FragmentActivity activity) {
        final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

}
