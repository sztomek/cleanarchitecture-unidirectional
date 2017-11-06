package hu.sztomek.archdemo.presentation.app;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class AppModule {

    private final Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    ImageLoaderConfiguration provideImageLoaderConfig(Context context) {
        return new ImageLoaderConfiguration.Builder(context)
                .build();
    }

    @Provides
    Timber.Tree povideTimberTree() {
        return new Timber.DebugTree();
    }

    public interface Exposes {
        Context getContext();
    }

}
