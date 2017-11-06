package hu.sztomek.archdemo.presentation.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.sztomek.archdemo.R;

public class SwipeRefreshWithText extends ConstraintLayout {

    private boolean showText;
    private String message;

    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.rvList)
    SwipeableRecyclerView rvList;

    public SwipeRefreshWithText(Context context) {
        this(context, null);
    }

    public SwipeRefreshWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LoadingRetryView,
                0, 0);

        try {
            showText = a.getBoolean(R.styleable.SwipeRefreshWithText_showText, false);
            if (a.hasValue(R.styleable.SwipeRefreshWithText_message)) {
                message = a.getString(R.styleable.SwipeRefreshWithText_message);
            } else {
                message = context.getString(R.string.default_empty_message);
            }
        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.layout_refresh, this);
        ButterKnife.bind(this);

        refresh();
    }

    private void refresh() {
        tvEmpty.setVisibility(showText ? View.VISIBLE : View.GONE);
        tvEmpty.setText(message);
        rvList.setVisibility(showText ? View.GONE : View.VISIBLE);
        invalidate();
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
        refresh();
    }

    public void setMessage(String message) {
        this.message = message;
        refresh();
    }

    public SwipeableRecyclerView getRecyclerView() {
        return rvList;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return srl;
    }
}
