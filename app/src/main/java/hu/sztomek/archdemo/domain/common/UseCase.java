package hu.sztomek.archdemo.domain.common;


import io.reactivex.Observable;

public interface UseCase<A extends Action> {

   Observable<Result> performAction(A action);

}
