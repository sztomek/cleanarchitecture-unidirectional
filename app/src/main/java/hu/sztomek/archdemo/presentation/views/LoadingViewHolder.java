package hu.sztomek.archdemo.presentation.views;

import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import hu.sztomek.archdemo.R;

public class LoadingViewHolder extends AdapterItemHolder<LoadingItem> {

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    public LoadingViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_loading);
    }

    @Override
    public void bind(LoadingItem item, ItemClickListener clickListener) {
        if (item.getLoadingRes() != 0) {
            tvMessage.setText(item.getLoadingRes());
        } else if (item.getLoadingMessage() != null) {
            tvMessage.setText(item.getLoadingMessage());
        }
    }

}
