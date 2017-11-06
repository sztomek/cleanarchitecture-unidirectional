package hu.sztomek.archdemo.presentation.app;

import javax.inject.Singleton;

import dagger.Component;
import hu.sztomek.archdemo.common.ThreadModule;
import hu.sztomek.archdemo.data.di.ApiModule;
import hu.sztomek.archdemo.data.di.NetworkModule;
import hu.sztomek.archdemo.domain.di.UseCaseModule;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ApiModule.class,
        ThreadModule.class,
        UseCaseModule.class
            })
public interface AppComponent extends AppComponentInjects, AppComponentExposes{
    final class Initializer {

        static public AppComponent init(final DemoApplication app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .networkModule(new NetworkModule())
                    .apiModule(new ApiModule())
                    .threadModule(new ThreadModule())
                    .useCaseModule(new UseCaseModule())
                    .build();
        }

        private Initializer() {
        }
    }
}
