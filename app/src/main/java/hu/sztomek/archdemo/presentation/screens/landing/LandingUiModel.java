package hu.sztomek.archdemo.presentation.screens.landing;

import hu.sztomek.archdemo.presentation.common.UiModel;
import hu.sztomek.archdemo.presentation.common.UserModel;

class LandingUiModel implements UiModel {

    private int selectedMenuId;
    private UserModel userModel;

    public int getSelectedMenuId() {
        return selectedMenuId;
    }

    public void setSelectedMenuId(int selectedMenuId) {
        this.selectedMenuId = selectedMenuId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String toString() {
        return "LandingUiModel{" +
                "selectedMenuId=" + selectedMenuId +
                ", userModel=" + userModel +
                '}';
    }
}
