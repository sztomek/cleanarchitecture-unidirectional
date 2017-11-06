package hu.sztomek.archdemo.presentation.screens.landing.timezone.list;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import hu.sztomek.archdemo.R;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.model.TimezoneModel;
import hu.sztomek.archdemo.presentation.views.AdapterItem;
import hu.sztomek.archdemo.presentation.views.AdapterItemHolder;
import hu.sztomek.archdemo.presentation.views.AutoLoadingAdapter;
import hu.sztomek.archdemo.presentation.views.ItemClickListener;
import hu.sztomek.archdemo.presentation.views.SwipeableViewHolder;
import io.reactivex.Observable;

public class TimezoneAdapter extends AutoLoadingAdapter implements Filterable {

    private final SimpleDateFormat dateFormat;
    private final ItemClickListener clickListener;
    private List<AdapterItem> original;

    public TimezoneAdapter(ItemClickListener clickListener) {
        super(clickListener);
        this.clickListener = clickListener;
        dateFormat = new SimpleDateFormat("kk:mm");
    }

    @Override
    protected AdapterItemHolder createViewHolderFor(ViewGroup parent, int viewType) {
        if (viewType == TimezoneModelAdapterItem.ITEM_TIMEZONE) {
            return new ViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterItemHolder holder, int position) {
        holder.bind(data.get(position), clickListener);
    }

    @Override
    public void onViewAttachedToWindow(AdapterItemHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).resetView();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (original == null) {
                    original = new ArrayList<>(data);
                }
                FilterResults filterResults = new FilterResults();
                List<AdapterItem> filtered = Observable.fromIterable(original)
                        .filter(item -> (item instanceof TimezoneModelAdapterItem) && ((TimezoneModelAdapterItem) item).getData().getName().contains(charSequence))
                        .toList()
                        .blockingGet();
                filterResults.count = filtered.size();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (charSequence == null || charSequence.length() == 0) {
                    setData(original);
                    original = null;
                } else {
                    setData(((List<AdapterItem>) filterResults.values));
                }
            }
        };
    }

    class ViewHolder extends AdapterItemHolder<TimezoneModelAdapterItem> implements SwipeableViewHolder {

        @BindView(R.id.clTimezone)
        ViewGroup clTimeZone;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCity)
        TextView tvCity;
        @BindView(R.id.tvDifference)
        TextView tvDifference;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        private ItemClickListener listener;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_timezone_swipe_to_delete);
        }

        @Override
        public void onSwipe() {
            if (listener != null) {
                listener.onDeleteClicked(getAdapterPosition());
            }
        }

        @Override
        public void onDraw(float dX) {
            clTimeZone.setTranslationX(dX);
            ivDelete.setAlpha(Math.abs(dX / clTimeZone.getWidth()));
        }

        public void reset() {
            resetView();
        }

        void resetView() {
            clTimeZone.setTranslationX(0f);
            ivDelete.setAlpha(0f);
        }


        @Override
        public void bind(TimezoneModelAdapterItem item, ItemClickListener listener) {
            resetView();
            this.listener = listener;
            clTimeZone.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            final TimezoneModel tzm = item.getData();
            tvName.setText(tzm.getName());
            tvCity.setText(tzm.getCity());
            int difference = Integer.parseInt(tzm.getDifference());
            tvDifference.setText("GMT " + (difference >= 0 ? "+" : "-") + tzm.getDifference());
            tvTime.setText(calculateCurrentTime(difference));
        }

        private String calculateCurrentTime(int diffInHours) {
            final Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, diffInHours);
            return dateFormat.format(calendar.getTime());
        }
    }

}
