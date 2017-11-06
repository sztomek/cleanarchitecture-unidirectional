package hu.sztomek.archdemo.presentation.views;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class AdapterItemHolder<I extends AdapterItem> extends RecyclerView.ViewHolder {

    public AdapterItemHolder(@NonNull ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(I item, ItemClickListener clickListener);

}