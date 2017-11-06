package hu.sztomek.archdemo.domain.usecases;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import hu.sztomek.archdemo.data.datasource.AuthService;
import hu.sztomek.archdemo.data.datasource.IAuthService;
import hu.sztomek.archdemo.data.datasource.RestService;
import hu.sztomek.archdemo.data.model.Timezone;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.TimezonePayload;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.edit.TimezoneEditUiActions;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class TimezoneGetUseCaseTest {

    @Mock
    IAuthService authService;
    @Mock
    RestService restService;
    @Mock
    FirebaseAuth auth;

    private TimezoneGetUseCase useCase;
    public static final String TEST_USER_ID = "userid";
    public static final String TEST_TIMEZONE_ID = "tzid";
    public static final String TEST_TOKEN = "token";
    public static final Timezone TIMEZONE;
    public static final TimezonePayload PAYLOAD;
    public static final RuntimeException NOPE = new RuntimeException("nope");

    static {
        TIMEZONE = new Timezone();
        TIMEZONE.setCity("City");
        TIMEZONE.setName("Name");
        TIMEZONE.setDiff(2);

        PAYLOAD = new TimezonePayload();
        PAYLOAD.setCity(TIMEZONE.getCity());
        PAYLOAD.setName(TIMEZONE.getName());
        PAYLOAD.setDifference(TIMEZONE.getDiff());
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        useCase = new TimezoneGetUseCase(Schedulers.io(), Schedulers.io(), restService, authService);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doActionHappyCase() throws Exception {
        Mockito.when(authService.getAuthToken()).thenReturn(Single.just(TEST_TOKEN));
        Mockito.when(restService.getTimezoneForUser(TEST_USER_ID, TEST_TIMEZONE_ID, TEST_TOKEN))
                .thenReturn(Observable.just(TIMEZONE));

        TimezoneEditUiActions.GetTimezoneAction action = TimezoneEditUiActions.get(TEST_USER_ID, TEST_TIMEZONE_ID);
        TestObserver<Result> testObs = useCase.doAction(action)
                .test();
        testObs.awaitTerminalEvent();
        testObs.assertNoErrors();
        testObs.assertValue(Result.success(action, PAYLOAD));
    }

    @Test
    public void doActionTokenException() throws Exception {
        Mockito.when(auth.getCurrentUser()).thenThrow(NOPE);
        AuthService spyAuth = Mockito.spy(new AuthService(auth));
        Mockito.when(restService.getTimezoneForUser(TEST_USER_ID, TEST_TIMEZONE_ID, TEST_TOKEN))
                .thenReturn(Observable.just(TIMEZONE));

        TimezoneEditUiActions.GetTimezoneAction action = TimezoneEditUiActions.get(TEST_USER_ID, TEST_TIMEZONE_ID);
        TimezoneGetUseCase spyUseCase = Mockito.spy(new TimezoneGetUseCase(Schedulers.io(), Schedulers.io(), restService, spyAuth));
        TestObserver<Result> test = spyUseCase.doAction(action).test();
        test.awaitTerminalEvent();
        test.assertNoErrors();
        test.assertValue(Result.error(action, NOPE));
    }

    @Test
    public void doActionRestException() throws Exception {
        Mockito.when(authService.getAuthToken()).thenReturn(Single.just(TEST_TOKEN));
        Mockito.when(restService.getTimezoneForUser(TEST_USER_ID, TEST_TIMEZONE_ID, TEST_TOKEN))
                .thenThrow(NOPE);

        TimezoneGetUseCase spyUseCase = Mockito.spy(new TimezoneGetUseCase(Schedulers.io(), Schedulers.io(), restService, authService));
        TimezoneEditUiActions.GetTimezoneAction action = TimezoneEditUiActions.get(TEST_USER_ID, TEST_TIMEZONE_ID);
        TestObserver<Result> test = spyUseCase.doAction(action).test();
        test.awaitTerminalEvent();
        test.assertNoErrors();
        test.assertValue(Result.error(action, NOPE));
    }



}