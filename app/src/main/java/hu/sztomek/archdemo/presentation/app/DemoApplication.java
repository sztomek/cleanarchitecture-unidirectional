package hu.sztomek.archdemo.presentation.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import javax.inject.Inject;

import timber.log.Timber;

public class DemoApplication extends Application {

    @Inject
    ImageLoaderConfiguration imageLoaderConfig;
    @Inject
    Timber.Tree tree;
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.Initializer.init(this);
        appComponent.inject(this);

        ImageLoader.getInstance().init(imageLoaderConfig);
        Timber.plant(tree);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
