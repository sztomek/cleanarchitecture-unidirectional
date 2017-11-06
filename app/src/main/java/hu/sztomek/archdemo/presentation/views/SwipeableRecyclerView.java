package hu.sztomek.archdemo.presentation.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

public class SwipeableRecyclerView extends RecyclerView {

    public SwipeableRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = createSwipeItemTouchHelper();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(this);
    }

    private ItemTouchHelper.SimpleCallback createSwipeItemTouchHelper() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof SwipeableViewHolder) {
                    ((SwipeableViewHolder) viewHolder).onSwipe();
                }
            }

            @Override
            public float getSwipeThreshold(ViewHolder viewHolder) {
                return 0.8f;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
               if (viewHolder instanceof SwipeableViewHolder) {
                   ((SwipeableViewHolder) viewHolder).onDraw(dX);
               }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
    }
}
