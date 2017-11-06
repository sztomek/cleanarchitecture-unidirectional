package hu.sztomek.archdemo.presentation.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hu.sztomek.archdemo.R;

public class LoadingRetryView extends ConstraintLayout {

    public static final int STATE_IDLE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_RETRY = 3;

    @IntDef({STATE_IDLE, STATE_LOADING, STATE_RETRY})
    public @interface State {}

    public interface RetryListener {
        void onRetryClick();
    }

    private String loadingMessage;
    private String retryMessage;
    private boolean showLoadingMessage;
    private boolean showRetryMessage;
    private RetryListener retryListener;
    private @State int state;
    private View lastInflated;

    public LoadingRetryView(Context context) {
        this(context, null);
    }

    public LoadingRetryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingRetryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LoadingRetryView,
                defStyleAttr, 0);

        try {
            if (a.hasValue(R.styleable.LoadingRetryView_loadingMessage)) {
                loadingMessage = a.getString(R.styleable.LoadingRetryView_loadingMessage);
            } else {
                loadingMessage = context.getString(R.string.default_loading_message);
            }
            if (a.hasValue(R.styleable.LoadingRetryView_retryMessage)) {
                retryMessage = a.getString(R.styleable.LoadingRetryView_retryMessage);
            } else {
                retryMessage = context.getString(R.string.default_loading_message);
            }
            showLoadingMessage = a.getBoolean(R.styleable.LoadingRetryView_showLoadingMessage, true);
            showRetryMessage = a.getBoolean(R.styleable.LoadingRetryView_showRetryMessage, true);
            state = a.getInteger(R.styleable.LoadingRetryView_state, 0);
        } finally {
            a.recycle();
        }
    }

    private void refresh() {
        if (lastInflated != null) {
            removeView(lastInflated);
        }

        switch (state) {
            case STATE_LOADING: {
                lastInflated = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading, this, false);
                final TextView tvLoadingMessage = lastInflated.findViewById(R.id.tvLoadingMessage);
                tvLoadingMessage.setVisibility(showLoadingMessage ? View.VISIBLE : View.INVISIBLE);
                tvLoadingMessage.setText(loadingMessage);
                break;
            }
            case STATE_RETRY: {
                lastInflated = LayoutInflater.from(getContext()).inflate(R.layout.layout_retry, this, false);
                final TextView tvRetryMessage = lastInflated.findViewById(R.id.tvRetryMessage);
                tvRetryMessage.setVisibility(showRetryMessage ? View.VISIBLE : View.INVISIBLE);
                tvRetryMessage.setText(retryMessage);
                if (retryListener != null) {
                    final OnClickListener onClickListener = new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            retryListener.onRetryClick();
                        }
                    };
                    tvRetryMessage.setOnClickListener(onClickListener);
                    lastInflated.findViewById(R.id.ivRetryImage).setOnClickListener(onClickListener);
                }
                break;
            }
            case STATE_IDLE:
            default: {
                lastInflated = null;
                break;
            }
        }

        if (lastInflated != null) {
            addView(lastInflated);
        }

        invalidate();
    }

    public @State int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
        refresh();
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        refresh();
    }

    public String getRetryMessage() {
        return retryMessage;
    }

    public void setRetryMessage(String retryMessage) {
        this.retryMessage = retryMessage;
        refresh();
    }

    public boolean isShowLoadingMessage() {
        return showLoadingMessage;
    }

    public void setShowLoadingMessage(boolean showLoadingMessage) {
        this.showLoadingMessage = showLoadingMessage;
        refresh();
    }

    public boolean isShowRetryMessage() {
        return showRetryMessage;
    }

    public void setShowRetryMessage(boolean showRetryMessage) {
        this.showRetryMessage = showRetryMessage;
        refresh();
    }

    public RetryListener getRetryListener() {
        return retryListener;
    }

    public void setRetryListener(RetryListener retryListener) {
        this.retryListener = retryListener;
    }
}
