package com.shuja.recyclerviewdragndropitem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomOnClickView extends AppCompatImageView implements View.OnTouchListener {

    private ScaleAnim scaleAnim;

    public ZoomOnClickView(Context context, int id) {
        super(context);
        init(context, null);
    }

    public ZoomOnClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZoomOnClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        scaleAnim = new ScaleAnim(this);
        setOnTouchListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setClip(this);
    }

    public void setClip(View v) {
        if (v.getParent() == null) {
            return;
        }

        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
            ((ViewGroup) v).setClipToPadding(false);
        }

        if (v.getParent() instanceof View) {
            setClip((View) v.getParent());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ZoomOnClickView) v).startScale();
                break;

            case MotionEvent.ACTION_UP:
                ((ZoomOnClickView) v).stopScale();
                break;
        }
        return true;
    }

    protected void startScale() {
        scaleAnim.start();
    }

    protected void stopScale() {
        scaleAnim.stop();
    }
}
