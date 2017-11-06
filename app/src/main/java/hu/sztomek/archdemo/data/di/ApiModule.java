package hu.sztomek.archdemo.data.di;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.sztomek.archdemo.data.datasource.AuthService;
import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.IUserManager;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.data.datasource.UserManager;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    @Singleton
    @Provides
    IAuthService provideAuthService(FirebaseAuth auth) {
        return new AuthService(auth);
    }

    @Singleton
    @Provides
    IUserManager provideUserManager(FirebaseAuth auth) {
        return new UserManager(auth);
    }

    public interface Exposes {

        IUserManager getUserManager();

    }

}
