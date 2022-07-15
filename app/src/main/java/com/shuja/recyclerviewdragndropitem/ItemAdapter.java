package com.shuja.recyclerviewdragndropitem;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter, View.OnTouchListener {

    private List<ItemModel> mPersonList;
    OnItemClickListener mItemClickListener;
    private static final int TYPE_ITEM = 0;
    private final LayoutInflater mInflater;
    private final OnStartDragListener mDragStartListener;
    private Context mContext;
    private ScaleAnim scaleAnim;
    private MediaPlayer startSound, endSound;
    private View v;

    public ItemAdapter(Context context, List<ItemModel> list, OnStartDragListener dragListner) {
        this.mPersonList = list;
        this.mInflater = LayoutInflater.from(context);
        mDragStartListener = dragListner;
        mContext = context;
        startSound = MediaPlayer.create(context, R.raw.start);
        endSound =  MediaPlayer.create(context, R.raw.bear);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = mInflater.inflate(R.layout.person_item, viewGroup, false);
            return new VHItem(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public int getItemViewType(int position) {
            return TYPE_ITEM;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof VHItem) {
            ((VHItem) viewHolder).title.setText(mPersonList.get(i).getName());
            Picasso.with(mContext)
                    .load(mPersonList.get(i).getImagePath())
                    .placeholder(R.drawable.ic_profile)
                    .into(((VHItem) viewHolder).imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener ,ItemTouchHelperViewHolder{
        public TextView title;
        final private AppCompatImageView imageView;

        public VHItem(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.name);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnTouchListener(ItemAdapter.this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAbsoluteAdapterPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    protected void startScale() {
        scaleAnim.start();
    }

    protected void stopScale() {
        scaleAnim.stop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /*scaleAnim = new ScaleAnim(v);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
             //   startSound.start();
//                startScale();
                break;

            case MotionEvent.ACTION_UP:
//                endSound.start();
//                stopScale();
                break;
        }*/
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        /*mPersonList.remove(position);
        notifyItemRemoved(position);*/
    }

    @Override
    public void onItemLongClick(View v) {
        this.v = v;
        //startSound.start();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < mPersonList.size() && toPosition < mPersonList.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    Collections.swap(mPersonList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    Collections.swap(mPersonList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    public void updateList(List<ItemModel> list) {
        mPersonList = list;
        notifyDataSetChanged();
    }
}