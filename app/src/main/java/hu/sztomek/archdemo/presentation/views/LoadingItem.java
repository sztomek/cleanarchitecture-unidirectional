package hu.sztomek.archdemo.presentation.views;

import android.support.annotation.StringRes;

public class LoadingItem implements AdapterItem {

    public static final int TYPE_LOADING = 9999;

    private @StringRes int loadingRes;
    public String loadingMessage;

    public LoadingItem(@StringRes int loadingMessage) {
        this.loadingRes = loadingMessage;
    }

    public LoadingItem(String message) {
        this.loadingMessage = message;
    }


    public @StringRes int getLoadingRes() {
        return loadingRes;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    @Override
    public int getItemViewType() {
        return TYPE_LOADING;
    }

    @Override
    public String getKey() {
        return "loadingitemkey";
    }

}
