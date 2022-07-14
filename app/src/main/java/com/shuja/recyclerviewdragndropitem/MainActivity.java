package com.shuja.recyclerviewdragndropitem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnStartDragListener, View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private boolean isOpened = false;
    private FloatingActionButton fab;
    private AppCompatButton subtract_button, add_button;
    private View touch_view;
    private float addButtonWidth, subtractButtonWidth;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        subtract_button = (AppCompatButton) findViewById(R.id.subtract_button);
        add_button = (AppCompatButton) findViewById(R.id.add_button);
        touch_view = (View) findViewById(R.id.touch_view);

        //Getting the width of views that have to be animated
        //In our case the add and subtract buttons width isn 50 dp
        //Therefore we convert dp to pixels for different screen sizes support
        addButtonWidth = convertDpToPx(50f);
        subtractButtonWidth = convertDpToPx(50f);

        touch_view.setOnTouchListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpened) {
                    closeQuantityBox();
                } else {
                    openQuantityBox();
                }
                isOpened = !isOpened;
            }
        });

        List<ItemModel> list = Utility.getListPerson();

//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ItemAdapter mAdapter = new ItemAdapter(this, list, this);
        ItemTouchHelper.Callback callback =
                new EditItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);


        //For hiding the view on recycler scroll
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Translate to zero for bringing views back to their original positions
                    fab.animate().translationX(0).setDuration(300);
                } else {
                    //We've given 60 in translationX for fully hiding the view
                    //50dp is width of button and 10dp is margin, so it 60dp
                    fab.animate().translationX(convertDpToPx(60)).setDuration(300);
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isOpened) {
                closeQuantityBox();
            }
            isOpened = !isOpened;
        }
        return true;
    }

    public void openQuantityBox() {
        touch_view.setVisibility(View.VISIBLE);
        add_button.setVisibility(View.VISIBLE);
        subtract_button.setVisibility(View.VISIBLE);

        //Negate(-) the value of with for animating the view from LTR
        add_button.animate().translationX(-(addButtonWidth + subtractButtonWidth)).setDuration(400).setInterpolator(new CustomBounce(0.3, 10));
        subtract_button.animate().translationX(-subtractButtonWidth).setDuration(200).setInterpolator(new CustomBounce(0.3, 10));
    }

    public void closeQuantityBox() {
        touch_view.setVisibility(View.GONE);

        //Translate to zero for bringing views back to their original positions
        add_button.animate().translationX(0).setDuration(100).setInterpolator(null);
        subtract_button.animate().translationX(0).setDuration(200).setInterpolator(null);

        //Delay is added so that the animating items should be invisible when the animation is done
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add_button.setVisibility(View.GONE);
                subtract_button.setVisibility(View.GONE);
            }
        }, 200);
    }

    public float convertDpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onStopDrag(RecyclerView.ViewHolder viewHolder) {

    }
}
