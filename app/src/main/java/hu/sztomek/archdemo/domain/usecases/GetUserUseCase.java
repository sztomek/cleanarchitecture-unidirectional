package hu.sztomek.archdemo.domain.usecases;

import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.domain.common.BaseUseCase;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.UserProfilePayload;
import hu.sztomek.archdemo.presentation.screens.landing.LandingUiActions;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetUserUseCase extends BaseUseCase<LandingUiActions.GetProfileAction> {

    private final RestService restService;
    private final IAuthService authService;

    public GetUserUseCase(Scheduler workload, Scheduler delivery, RestService restService, IAuthService authService) {
        super(workload, delivery);
        this.restService = restService;
        this.authService = authService;
    }

    @Override
    protected Observable<Result> doAction(LandingUiActions.GetProfileAction action) {
        return authService.getAuthToken()
                .observeOn(workload)
                .flatMapObservable(s ->
                        restService.getUser(action.getUid(), s)
                        .map(profile -> {
                            UserProfilePayload payload = new UserProfilePayload();
                            payload.setAvatar(profile.getAvatar());
                            payload.setEmail(profile.getEmail());
                            payload.setName(profile.getName());
                            return Result.success(action, payload);
                        })
                ).onErrorReturn(t -> Result.error(action, t));
    }
}
