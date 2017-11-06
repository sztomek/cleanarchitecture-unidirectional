package hu.sztomek.archdemo.domain.common;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public abstract class BaseUseCase<A extends Action> implements UseCase<A> {

    protected final Scheduler workload;
    protected final Scheduler delivery;

    protected BaseUseCase(Scheduler workload, Scheduler delivery) {
        this.workload = workload;
        this.delivery = delivery;
    }

    @Override
    public Observable<Result> performAction(A action) {
        return doAction(action)
                .subscribeOn(workload)
                .observeOn(delivery);
    }

    protected abstract Observable<Result> doAction(A action);
}
