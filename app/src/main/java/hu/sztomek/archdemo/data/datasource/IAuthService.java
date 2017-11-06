package hu.sztomek.archdemo.data.datasource;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface IAuthService {

    Completable signInWithEmailAndPassword(final String email, final String password);
    Completable sendPasswordReset(final String email);
    Completable registerWithEmailAndPassword(final String email, final String password);
    Single<String> getAuthToken();
    Completable signInWithGoogle(String token);
    Completable signOut();

}
