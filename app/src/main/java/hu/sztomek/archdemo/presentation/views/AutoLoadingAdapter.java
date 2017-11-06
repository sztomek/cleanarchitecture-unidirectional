package hu.sztomek.archdemo.presentation.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

public abstract class AutoLoadingAdapter extends RecyclerView.Adapter<AdapterItemHolder> {

    public interface LoadMoreListener {
        void onLoadMore();
    }

    private final ItemClickListener clickListener;
    private LoadMoreListener loadListener;
    private final RecyclerView.OnScrollListener scrollListener;
    private volatile boolean isLoading;
    private int visibleThreshold;
    protected List<AdapterItem> data;
    private RecyclerView recyclerView;
    private boolean autoLoadEnabled;

    protected AutoLoadingAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
        data = new ArrayList<>();
        visibleThreshold = 3;
        scrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisible = layoutManager.findLastVisibleItemPosition();
                    if (autoLoadEnabled && !isLoading && data.size() <= (lastVisible + visibleThreshold)) {
                        triggerLoading();
                    }
                } else {
                    throw new UnsupportedOperationException("Can only be used with LinearLayoutManager");
                }
            }

        };
    }

    private void triggerLoading() {
        Timber.d(getClass().getSimpleName() + ": triggering load in adapter data: ["+data+"]");
        isLoading = true;
        if (loadListener != null) {
            loadListener.onLoadMore();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView.removeOnScrollListener(scrollListener);
        this.recyclerView = null;
    }
    
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemViewType();
    }

    @Override
    public AdapterItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (LoadingItem.TYPE_LOADING == viewType) {
            return new LoadingViewHolder(parent);
        }
        return createViewHolderFor(parent, viewType);
    }

    @Override
    public void onBindViewHolder(AdapterItemHolder holder, int position) {
        holder.bind(data.get(position), clickListener);
    }

    public String getLastItemId() {
        if (data.isEmpty()) {
            return null;
        }
        return Observable.fromIterable(data)
                .filter(item -> !(item instanceof LoadingItem))
                .blockingLast().getKey();
    }

    public void setData(List<AdapterItem> newData) {
        this.data.clear();
        if (newData != null) {
            this.data.addAll(newData);
        }
        setLoading(false);
        notifyDataSetChanged();
    }

    public void delete(int position) {
        if (data != null && data.size() > position) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setEnableAutoLoad(boolean enableEndlessLoad) {
        this.autoLoadEnabled = enableEndlessLoad;
    }

    public AdapterItem getItem(int item) {
        if (data != null && data.size() > item) {
            return data.get(item);
        }
        return null;
    }

    public void setLoadListener(LoadMoreListener loadListener) {
        this.loadListener = loadListener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    protected void setLoading(boolean loading) {
        isLoading = loading;
    }

    protected abstract AdapterItemHolder createViewHolderFor(ViewGroup parent, int viewType);

}
