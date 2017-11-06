package hu.sztomek.archdemo.presentation.views;

public interface SwipeableViewHolder {

    void reset();
    void onSwipe();

    void onDraw(float dX);
}

