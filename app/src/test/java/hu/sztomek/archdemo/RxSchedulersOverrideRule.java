package hu.sztomek.archdemo;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulersOverrideRule implements TestRule {

    private final Function<Callable<Scheduler>, Scheduler> mRxAndroidSchedulersHook =
            schedulerCallable -> getScheduler();

    private final Function<Scheduler, Scheduler> mRxJavaImmediateScheduler =
            scheduler -> getScheduler();

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.reset();
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(mRxAndroidSchedulersHook);

                RxJavaPlugins.reset();
                RxJavaPlugins.setIoSchedulerHandler(mRxJavaImmediateScheduler);
                RxJavaPlugins.setNewThreadSchedulerHandler(mRxJavaImmediateScheduler);

                base.evaluate();

                RxAndroidPlugins.reset();
                RxJavaPlugins.reset();
            }
        };
    }

    public Scheduler getScheduler() {
        return Schedulers.trampoline();
    }

}
