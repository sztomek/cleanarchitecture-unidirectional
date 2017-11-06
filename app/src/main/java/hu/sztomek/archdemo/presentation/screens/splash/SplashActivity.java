package hu.sztomek.archdemo.presentation.screens.splash;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserActivity;
import hu.sztomek.archdemo.presentation.screens.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startScreen(LoginActivity.class);
        } else {
            startScreen(CheckUserActivity.class);
        }
    }

    private void startScreen(Class<? extends Activity> clazz) {
        final Intent starter = new Intent(this, clazz);
        startActivity(starter);
        finish();
    }
}
