package hu.sztomek.archdemo.common;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class ThreadModule {

    public static final String NAMED_WORKER = "worker";
    public static final String NAMED_DELIVERY = "delivery";

    @Named(NAMED_WORKER)
    @Singleton
    @Provides
    Scheduler provideWorker() {
        return Schedulers.io();
    }

    @Named(NAMED_DELIVERY)
    @Singleton
    @Provides
    Scheduler provideDelivery() {
        return AndroidSchedulers.mainThread();
    }

    public interface Exposes {

        @Named(NAMED_WORKER)
        Scheduler getWorker();
        @Named(NAMED_DELIVERY)
        Scheduler getDelivery();

    }

}
