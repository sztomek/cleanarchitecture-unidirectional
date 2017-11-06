package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.data.model.Profile;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.presentation.screens.check_user.CheckUserActions;
import hu.sztomek.archdemo.presentation.screens.check_user.UserProfile;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class RegisterUserUseCase extends BaseUseCase<CheckUserActions.RegisterUserRecord> {

    private final IAuthService authService;
    private final RestService restService;

    public RegisterUserUseCase(Scheduler workload, Scheduler delivery, IAuthService authService, RestService restService) {
        super(workload, delivery);
        this.authService = authService;
        this.restService = restService;
    }

    @Override
    protected Observable<Result> doAction(CheckUserActions.RegisterUserRecord action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s -> {
                    final Profile userProfile = new Profile();
                    final UserProfile profile = action.getUserProfile();
                    userProfile.setEmail(profile.getEmail());
                    userProfile.setName(profile.getDisplayName());
                    userProfile.setAvatar(profile.getAvatarUrl());
                    return restService.saveUser(userProfile, action.getUid(), s)
                            .map(profile1 -> Result.success(action, new SuccessPayload(true)));
                })
                .onErrorReturn(t -> Result.error(action, t));
    }

}
