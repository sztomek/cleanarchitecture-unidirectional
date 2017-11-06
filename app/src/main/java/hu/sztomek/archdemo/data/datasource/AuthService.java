package hu.sztomek.archdemo.data.datasource;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import hu.sztomek.archdemo.domain.common.UserNotLoggedInException;
import hu.sztomek.archdemo.domain.common.UnknownError;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthService implements IAuthService {

    private final FirebaseAuth auth;

    public AuthService(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public Completable signInWithEmailAndPassword(final String email, final String password) {
        return Completable.create(e -> auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!e.isDisposed()) {
                if (task.isSuccessful()) {
                    e.onComplete();
                } else {
                    if (task.getException() != null) {
                        e.onError(task.getException());
                    } else {
                        e.onError(new UnknownError());
                    }
                }
            }
        }));
    }

    @Override
    public Completable sendPasswordReset(final String email) {
        return Completable.create(e -> auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (!e.isDisposed()) {
                if (task.isSuccessful()) {
                    e.onComplete();
                } else {
                    if (task.getException() != null) {
                        e.onError(task.getException());
                    } else {
                        e.onError(new UnknownError());
                    }
                }
            }
        }));
    }

    @Override
    public Completable registerWithEmailAndPassword(final String email, final String password) {
        return Completable.create(e -> auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!e.isDisposed()) {
                if (task.isSuccessful()) {
                    e.onComplete();
                } else {
                    if (task.getException() != null) {
                        e.onError(task.getException());
                    } else {
                        e.onError(new UnknownError());
                    }
                }
            }
        }));
    }

    @Override
    public Single<String> getAuthToken() {
        return Single.create(e -> {
            final FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser == null) {
                throw new UserNotLoggedInException();
            }

            currentUser.getIdToken(false).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    e.onSuccess(task.getResult().getToken());
                } else {
                    if (task.getException() != null) {
                        e.onError(task.getException());
                    } else {
                        e.onError(new UnknownError());
                    }
                }
            });
        });
    }

    @Override
    public Completable signInWithGoogle(final String token) {
        return Completable.create(e -> {
            final AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
            auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (!e.isDisposed()) {
                    if (task.isSuccessful()) {
                        e.onComplete();
                    } else {
                        if (task.getException() != null) {
                            e.onError(task.getException());
                        } else {
                            e.onError(new UnknownError());
                        }
                    }
                }
            });
        });
    }

    @Override
    public Completable signOut() {
        return Completable.create(e -> {
            auth.signOut();
            e.onComplete();
        });
    }
}
