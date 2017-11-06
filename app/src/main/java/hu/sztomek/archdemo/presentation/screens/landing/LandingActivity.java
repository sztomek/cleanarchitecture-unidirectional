package hu.sztomek.archdemo.presentation.screens.landing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.app.DemoApplication;
import hu.sztomek.archdemo.presentation.common.BaseActivity;
import hu.sztomek.archdemo.presentation.common.IPresenter;
import hu.sztomek.archdemo.presentation.common.UiState;
import hu.sztomek.archdemo.presentation.common.UserModel;
import hu.sztomek.archdemo.presentation.di.DaggerPresentationComponent;
import hu.sztomek.archdemo.presentation.di.PresentationModule;
import hu.sztomek.archdemo.presentation.di.RouterModule;
import hu.sztomek.archdemo.presentation.navigation.IRouter;

public class LandingActivity extends BaseActivity<LandingUiModel>
        implements NavigationView.OnNavigationItemSelectedListener, Contract.View {

    private static final String KEY_SELECTED_ID = "selectedMenuId";
    private static final String TAG_CURRENT_FRAGMENT = "currentFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private TextView tvName;
    private TextView tvEmail;
    private ImageView ivAvatar;

    @Inject
    LandingPresenter presenter;
    @Inject
    IRouter router;

    @Override
    protected IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onGetLastCustomNonConfigurationInstance(Object instance) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(null, ((LandingPresenter) instance)))
                .build()
                .inject(this);
    }

    @Override
    protected void createPresenter(UiState<LandingUiModel> initialState) {
        DaggerPresentationComponent.builder()
                .appComponent(DemoApplication.getAppComponent())
                .routerModule(new RouterModule(this))
                .presentationModule(new PresentationModule(initialState, null))
                .build()
                .inject(this);
    }

    @Override
    protected void saveStateToBundle(Bundle persistence) {
        if (presenter.getLastState().getData() != null) {
            persistence.putInt(KEY_SELECTED_ID, presenter.getLastState().getData().getSelectedMenuId());
        }
    }

    @Override
    protected UiState<LandingUiModel> restoreStateFromBundle(Bundle persistence) {
        return UiState.<LandingUiModel>idle().toBuilder().setData(new LandingUiModel()).build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        View navigationHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvName = navigationHeader.findViewById(R.id.tvName);
        tvEmail = navigationHeader.findViewById(R.id.tvEmail);
        ivAvatar = navigationHeader.findViewById(R.id.ivProfile);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        int menuId = savedInstanceState == null ? R.id.nav_timezones : savedInstanceState.getInt(KEY_SELECTED_ID, R.id.nav_timezones);
        navigationView.setCheckedItem(menuId);
        navigationView.getMenu().performIdentifierAction(menuId, 0);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout: {
                events.onNext(LandingUiEvents.logout(R.id.nav_logout));
                break;
            }
            case R.id.nav_timezones: {
                events.onNext(LandingUiEvents.menuSelected(R.id.nav_timezones));
                break;
            }
            default: {
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void render(@Nonnull UiState<LandingUiModel> state) {
        if (state.getData() != null) {
            final UserModel userModel = state.getData().getUserModel();
            if (userModel != null) {
                tvEmail.setText(userModel.getEmail());
                tvName.setText(userModel.getName());
                if (userModel.getAvatar() != null) {
                    ImageLoader.getInstance().displayImage(userModel.getAvatar(), ivAvatar);
                }
            }

            switch (state.getData().getSelectedMenuId()) {
                case R.id.nav_logout: {
                    Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();
                    router.restart();
                    break;
                }
                case R.id.nav_timezones:
                default: {
                    router.showTimezoneList();
                    toolbar.setTitle(R.string.timezones);
                    break;
                }
            }
        }
    }
}
