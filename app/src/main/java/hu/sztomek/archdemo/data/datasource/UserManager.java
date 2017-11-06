package hu.sztomek.archdemo.data.datasource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserManager implements IUserManager {

    private final FirebaseAuth auth;

    public UserManager(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public String getUserId() {
        final FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }

    @Override
    public String getUserName() {
        final FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser == null ? null : currentUser.getDisplayName() == null ? "N/A" : currentUser.getDisplayName();
    }

    @Override
    public String getAvatar() {
        final FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser == null ? null : currentUser.getPhotoUrl() == null ? "N/A" : currentUser.getPhotoUrl().toString();
    }

    @Override
    public String getUserEmail() {
        final FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser == null ? null : currentUser.getEmail() == null ? "N/A" : currentUser.getEmail();
    }
}
