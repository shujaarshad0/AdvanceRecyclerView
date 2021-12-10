package com.shuja.recyclerviewdragndropitem;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

