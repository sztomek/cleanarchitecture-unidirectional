package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import hu.sztomek.archdemo.RxSchedulersOverrideRule;
import hu.sztomek.archdemo.domain.common.Result;
import hu.sztomek.archdemo.domain.payloads.SuccessPayload;
import hu.sztomek.archdemo.domain.payloads.TimezonePayload;
import hu.sztomek.archdemo.domain.usecases.TimezoneDeleteUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneGetUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneSaveUseCase;
import hu.sztomek.archdemo.domain.usecases.TimezoneUpdateUseCase;
import hu.sztomek.archdemo.presentation.common.DeleteFailedUiError;
import hu.sztomek.archdemo.presentation.common.FormValidationUiError;
import hu.sztomek.archdemo.presentation.common.UiError;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

public class TimezoneEditPresenterTest {

    public static final String USER_ID = "userid";
    public static final String TIMEZONE_ID = "tzid";
    public static final DeleteTimezoneAction DELETE_TIMEZONE_ACTION = TimezoneEditUiActions.delete(USER_ID, TIMEZONE_ID);
    public static final TimezoneEditUiActions.GetTimezoneAction GET_TIMEZONE_ACTION = TimezoneEditUiActions.get(USER_ID, TIMEZONE_ID);
    public static final String CITY = "City";
    public static final String NAME = "Name";
    public static final TimezoneEditUiActions.SaveTimezoneAction SAVE_TIMEZONE_ACTION = TimezoneEditUiActions.save(
            new TimezoneEditUiActions.SaveTimezoneAction.Builder()
                    .setId(null)
                    .setUserId(USER_ID)
                    .setName(NAME)
                    .setCity(CITY)
                    .setDifference(0)
    );
    public static final int DIFFERENCE = 1;
    public static final TimezoneEditUiActions.SaveTimezoneAction UPDATE_TIMEZONE_ACTION = TimezoneEditUiActions.save(new TimezoneEditUiActions.SaveTimezoneAction.Builder()
            .setCity(CITY)
            .setName(NAME)
            .setDifference(DIFFERENCE)
            .setUserId(USER_ID)
            .setId(TIMEZONE_ID)
    );

    @Rule
    public final RxSchedulersOverrideRule rule = new RxSchedulersOverrideRule();

    @Mock
    TimezoneGetUseCase getUseCase;
    @Mock
    TimezoneSaveUseCase saveUseCase;
    @Mock
    TimezoneUpdateUseCase updateUseCase;
    @Mock
    TimezoneDeleteUseCase deleteUseCase;
    @Mock
    Contract.View view;

    private TimezoneEditPresenter presenter;
    private final PublishSubject<UiEvent> publishSubject = PublishSubject.create();
    private TestObserver<UiState<TimezoneEditUiModel>> test;
    private UiState<TimezoneEditUiModel> start;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final TimezoneEditUiModel startData = new TimezoneEditUiModel();
        final TimezoneModel startModel = new TimezoneModel();
        startModel.setOwnerId(USER_ID);
        startModel.setId(TIMEZONE_ID);
        startData.setTimezoneModel(startModel);
        start = UiState.<TimezoneEditUiModel>idle().toBuilder()
                .setData(startData).build();
        presenter = new TimezoneEditPresenter(start, getUseCase, saveUseCase, updateUseCase, deleteUseCase);
        Mockito.when(view.actionStream()).thenReturn(publishSubject);
        presenter.attach(view);
        test = presenter.stateStream().test();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
        presenter = null;
    }

    @Test
    public void getTimezoneHappyCase() throws Exception {
        final TimezonePayload payload = new TimezonePayload();
        payload.setCity(CITY);
        payload.setName(NAME);
        payload.setDifference(1);
        Mockito.when(getUseCase.performAction(GET_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(GET_TIMEZONE_ACTION, payload)));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.get(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .setData(new TimezoneEditUiModel())
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final TimezoneEditUiModel data = new TimezoneEditUiModel();
        final TimezoneModel timezoneModel = new TimezoneModel();
        timezoneModel.setDifference(String.valueOf(payload.getDifference()));
        timezoneModel.setCity(payload.getCity());
        timezoneModel.setName(payload.getName());
        timezoneModel.setId(TIMEZONE_ID);
        timezoneModel.setOwnerId(USER_ID);
        data.setTimezoneModel(timezoneModel);
        test.assertValueAt(2,
                loading
                    .toBuilder()
                    .setLoading(false)
                    .setData(data)
                    .build()
        );
    }

    @Test
    public void getTimezoneThrowException() throws Exception {
        final TimezonePayload payload = new TimezonePayload();
        payload.setCity(CITY);
        payload.setName(NAME);
        payload.setDifference(1);

        final NullPointerException nope = new NullPointerException("nope");
        Mockito.when(getUseCase.performAction(GET_TIMEZONE_ACTION))
                        .thenThrow(nope);

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.get(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new UiError(true, nope.getMessage()))
                        .build()
        );
    }

    @Test
    public void getTimezoneErrorResult() throws Exception {
        final TimezonePayload payload = new TimezonePayload();
        payload.setCity(CITY);
        payload.setName(NAME);
        payload.setDifference(1);
        final RuntimeException nope = new RuntimeException("nope");
        Mockito.when(getUseCase.performAction(GET_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.error(GET_TIMEZONE_ACTION, nope)));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.get(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .setData(new TimezoneEditUiModel())
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setError(new UiError(true, "nope"))
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void saveTimezoneHappyCase() throws Exception {
        Mockito.when(saveUseCase.performAction(SAVE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(SAVE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setId(null);
        tzm.setOwnerId(USER_ID);
        tzm.setCity(CITY);
        tzm.setName(NAME);
        tzm.setDifference(null);
        start.getData().setTimezoneModel(tzm);
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        loading.getData().setSuccess(true);
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void saveTimezoneEmptyNameCase() throws Exception {
        Mockito.when(saveUseCase.performAction(UPDATE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(UPDATE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setCity(CITY);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setOwnerId(USER_ID);
        tzm.setId(null);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new FormValidationUiError("blahblah"))
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void saveTimezoneEmptyCityCase() throws Exception {
        Mockito.when(saveUseCase.performAction(UPDATE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(UPDATE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setName(NAME);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setOwnerId(USER_ID);
        tzm.setId(null);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new FormValidationUiError("blahblah"))
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void saveTimezoneEmptyDiffCase() throws Exception {
        Mockito.when(saveUseCase.performAction(SAVE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(SAVE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setId(null);
        tzm.setOwnerId(USER_ID);
        tzm.setCity(CITY);
        tzm.setName(NAME);
        tzm.setDifference(null);
        start.getData().setTimezoneModel(tzm);
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));

        // should be success since we set default diff value to 0.
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        loading.getData().setSuccess(true);
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void saveTimezoneDiffIsNotANumber() throws Exception {
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setId(null);
        tzm.setOwnerId(USER_ID);
        tzm.setCity(CITY);
        tzm.setName(NAME);
        tzm.setDifference("Hello");
        start.getData().setTimezoneModel(tzm);
        test.assertValueAt(0,
                start
        );

        // should be success since we set default diff value to 0.
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start.toBuilder()
                        .setError(new FormValidationUiError("blahblah"))
                        .build()
        );
    }

    @Test
    public void saveTimezoneThrowException() throws Exception {
        final NullPointerException nope = new NullPointerException("nope");
        Mockito.when(saveUseCase.performAction(SAVE_TIMEZONE_ACTION))
                .thenThrow(nope);

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setName(NAME);
        tzm.setCity(CITY);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setOwnerId(USER_ID);
        tzm.setId(null);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new UiError(true, nope.getMessage()))
                        .build()
        );
    }

    @Test
    public void saveTimezoneErrorResult() throws Exception {
        final RuntimeException nope = new RuntimeException("nope");
        Mockito.when(saveUseCase.performAction(SAVE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.error(SAVE_TIMEZONE_ACTION, nope)));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setName(NAME);
        tzm.setCity(CITY);
        tzm.setDifference(String.valueOf(0));
        tzm.setOwnerId(USER_ID);
        tzm.setId(null);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setError(new UiError(true, "nope"))
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void updateTimezoneHappyCase() throws Exception {
        Mockito.when(updateUseCase.performAction(UPDATE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(UPDATE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setName(NAME);
        tzm.setCity(CITY);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setOwnerId(USER_ID);
        tzm.setId(TIMEZONE_ID);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        loading.getData().setSuccess(true);
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void updateTimezoneThrowException() throws Exception {
        final NullPointerException nope = new NullPointerException("nope");
        Mockito.when(updateUseCase.performAction(UPDATE_TIMEZONE_ACTION))
                .thenThrow(nope);

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        TimezoneModel tzm = new TimezoneModel();
        tzm.setId(TIMEZONE_ID);
        tzm.setOwnerId(USER_ID);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setCity(CITY);
        tzm.setName(NAME);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new UiError(true, nope.getMessage()))
                        .build()
        );
    }

    @Test
    public void updateTimezoneErrorResult() throws Exception {
        final RuntimeException nope = new RuntimeException("nope");
        Mockito.when(updateUseCase.performAction(UPDATE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.error(UPDATE_TIMEZONE_ACTION, nope)));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setName(NAME);
        tzm.setCity(CITY);
        tzm.setDifference(String.valueOf(DIFFERENCE));
        tzm.setOwnerId(USER_ID);
        tzm.setId(TIMEZONE_ID);
        publishSubject.onNext(TimezoneEditUiEvents.save(tzm));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setError(new UiError(true, "nope"))
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void deleteTimezoneHappyCase() throws Exception {
        Mockito.when(deleteUseCase.performAction(DELETE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.success(DELETE_TIMEZONE_ACTION, new SuccessPayload(true))));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.delete(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        loading.getData().setSuccess(true);
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setLoading(false)
                        .build()
        );
    }

    @Test
    public void deleteTimezoneThrowException() throws Exception {
        final NullPointerException nope = new NullPointerException("nope");
        Mockito.when(deleteUseCase.performAction(DELETE_TIMEZONE_ACTION))
                .thenThrow(nope);

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.delete(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(1,
                start
                        .toBuilder()
                        .setError(new UiError(true, nope.getMessage()))
                        .build()
        );
    }

    @Test
    public void deleteTimezoneErrorResult() throws Exception {
        final RuntimeException nope = new RuntimeException("nope");
        Mockito.when(deleteUseCase.performAction(DELETE_TIMEZONE_ACTION))
                .thenReturn(Observable.just(Result.error(DELETE_TIMEZONE_ACTION, nope)));

        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(0,
                start
        );
        publishSubject.onNext(TimezoneEditUiEvents.delete(USER_ID, TIMEZONE_ID));
        test.assertSubscribed();
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        final UiState<TimezoneEditUiModel> loading = start.toBuilder()
                .setLoading(true)
                .build();
        test.assertValueAt(1,
                loading
        );
        test.awaitCount(1);
        test.assertSubscribed();
        test.assertNotComplete();
        test.assertValueAt(2,
                loading
                        .toBuilder()
                        .setError(new DeleteFailedUiError("blabla"))
                        .setLoading(false)
                        .build()
        );
    }
}