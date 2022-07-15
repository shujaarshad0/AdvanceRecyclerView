package com.shuja.recyclerviewdragndropitem;

import android.view.View;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void onItemLongClick(View v);
}

