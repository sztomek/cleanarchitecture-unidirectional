package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseActivity;
import hu.sztomek.archdemo.presentation.common.DeleteFailedUiError;
import hu.sztomek.archdemo.presentation.common.FormValidationUiError;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiEvent;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import hu.sztomek.archdemo.presentation.views.LoadingRetryView;
import io.reactivex.Observable;

public class TimezoneEditActivity extends BaseActivity<TimezoneEditUiModel> {

    private static final String KEY_USER_ID = "userId";
    private static final String KEY_TIMEZONE_ID = "timezoneId";
    private UiEvent lastEvent;

    public static Intent forEdit(Context context, final String userId, final String timezoneId) {
        final Intent starter = new Intent(context, TimezoneEditActivity.class);
        final Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userId);
        args.putString(KEY_TIMEZONE_ID, timezoneId);
        starter.putExtras(args);
        return starter;
    }

    public static Intent forCreate(Context context, final String userId) {
        return forEdit(context, userId, null);
    }

    @BindView(R.id.lrv)
    LoadingRetryView loadingRetryView;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etDifference)
    EditText etDifference;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.tvError)
    TextView tvError;

    @Inject
    TimezoneEditPresenter presenter;
    @Inject
    IRouter router;

    @Override
    public void render(@Nonnull UiState<TimezoneEditUiModel> state) {
        setUiEnabled(false);
        tvError.setVisibility(View.INVISIBLE);
        if (state.isLoading()) {
            loadingRetryView.setState(LoadingRetryView.STATE_LOADING);
        } else if (state.getError() != null) {
            if (state.getError() instanceof FormValidationUiError) {
                tvError.setText(state.getError().getMessage());
                tvError.setVisibility(View.VISIBLE);
                loadingRetryView.setState(LoadingRetryView.STATE_IDLE);
            } else if (state.getError() instanceof DeleteFailedUiError) {
                Toast.makeText(this, state.getError().getMessage(), Toast.LENGTH_SHORT).show();
                loadingRetryView.setState(LoadingRetryView.STATE_IDLE);
            } else {
                loadingRetryView.setState(LoadingRetryView.STATE_RETRY);
                loadingRetryView.setRetryMessage(state.getError().getMessage());
            }
            setUiEnabled(true);
        } else {
            setUiEnabled(true);
            loadingRetryView.setState(LoadingRetryView.STATE_IDLE);
            if (state.getData() != null) {
                final TimezoneModel tzm = state.getData().getTimezoneModel();
                if (tzm != null) {
                    etName.setText(tzm.getName());
                    etCity.setText(tzm.getCity());
                    etDifference.setText(tzm.getDifference());
                }

                if (state.getData().isSuccess()) {
                    router.close();
                }
            }

        }
    }

    private void setUiEnabled(boolean enabled) {
        btnDelete.setEnabled(enabled);
        btnSubmit.setEnabled(enabled);
        etName.setEnabled(enabled);
        etCity.setEnabled(enabled);
        etDifference.setEnabled(enabled);
    }

    @Override
    public Observable<UiEvent> actionStream() {
        return getTimezoneId() == null || getUserId() == null
                ? super.actionStream()
                : events.mergeWith(Observable.just(TimezoneEditUiEvents.get(getUserId(), getTimezoneId())).delay(500, TimeUnit.MILLISECONDS));
    }

    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onGetLastCustomNonConfigurationInstance(Object instance) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(null, ((TimezoneEditPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<TimezoneEditUiModel> initialState) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(initialState, presenter))
                .build()
                .inject(this);
    }

    @Override
    protected void saveStateToBundle(Bundle persistence) {

    }

    @Override
    protected UiState<TimezoneEditUiModel> restoreStateFromBundle(Bundle persistence) {
        final TimezoneEditUiModel data = new TimezoneEditUiModel();
        final TimezoneModel tzm = new TimezoneModel();
        tzm.setId(getTimezoneId());
        tzm.setOwnerId(getUserId());
        data.setTimezoneModel(tzm);
        return UiState.<TimezoneEditUiModel>idle().toBuilder().setData(data).build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timezone);
        ButterKnife.bind(this);

        loadingRetryView.setRetryListener(() -> events.onNext(lastEvent == null ? TimezoneEditUiEvents.get(getUserId(), getTimezoneId()) : lastEvent));
        btnDelete.setVisibility(getTimezoneId() == null ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick(R.id.btnDelete)
    public void onClickDelete(View v) {
        lastEvent = TimezoneEditUiEvents.delete(getUserId(), getTimezoneId());
        events.onNext(lastEvent);
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmitClick(View v) {
        lastEvent = TimezoneEditUiEvents.save(createTimezoneModel());
        events.onNext(lastEvent);
    }
    
    private String getUserId() {
        return getIntent().getStringExtra(KEY_USER_ID);
    }
    
    private String getTimezoneId() {
        return getIntent().getStringExtra(KEY_TIMEZONE_ID);
    }

    private TimezoneModel createTimezoneModel() {
        final TimezoneModel timezoneModel = new TimezoneModel();
        timezoneModel.setOwnerId(getUserId());
        timezoneModel.setId(getTimezoneId());
        timezoneModel.setCity(etCity.getText().toString());
        timezoneModel.setName(etName.getText().toString());
        timezoneModel.setDifference(etDifference.getText().toString());
        return timezoneModel;
    }
}
