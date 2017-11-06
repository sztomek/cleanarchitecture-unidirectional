package hu.sztomek.archdemo.presentation.screens.check_user;

import hu.sztomek.archdemo.domain.common.Action;
import hu.sztomek.archdemo.domain.common.AuthenticatedAction;

public final class CheckUserActions {

    private CheckUserActions() {}

    public static Action exists(String authId) {
        return new CheckUserExists(authId);
    }

    public static Action createRecord(String authId, String displayName, String emailAddress, String avatarUrl) {
        UserProfile userProfile = new UserProfile();
        userProfile.setDisplayName(displayName);
        userProfile.setEmail(emailAddress);
        userProfile.setAvatarUrl(avatarUrl);
        return new RegisterUserRecord(authId, userProfile);
    }

    public static class CheckUserExists extends AuthenticatedAction {

        CheckUserExists(String authId) {
            super(authId);
        }

    }

    public static class RegisterUserRecord extends AuthenticatedAction {

        private final UserProfile userProfile;

        RegisterUserRecord(String authId, UserProfile userProfile) {
            super(authId);
            this.userProfile = userProfile;
        }

        public UserProfile getUserProfile() {
            return userProfile;
        }
    }

}
