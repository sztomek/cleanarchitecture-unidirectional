package hu.sztomek.archdemo.presentation.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import hu.sztomek.archdemo.common.PerScreen;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.navigation.Router;

@Module
public class RouterModule {

    private final FragmentActivity activity;

    public RouterModule(FragmentActivity activity) {
        this.activity = activity;
    }

    @PerScreen
    @Provides
    FragmentActivity provideActivity() {
        return activity;
    }

    @PerScreen
    @Provides
    IRouter provideRouter(FragmentActivity activity) {
        return new Router(activity);
    }

}
