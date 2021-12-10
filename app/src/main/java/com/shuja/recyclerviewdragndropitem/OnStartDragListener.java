package com.shuja.recyclerviewdragndropitem;


import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDragListener {

    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void onStopDrag(RecyclerView.ViewHolder viewHolder);

}
