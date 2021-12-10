package com.shuja.recyclerviewdragndropitem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    RecyclerView mRecyclerView;
    ItemTouchHelper mItemTouchHelper;
    boolean isOpened = false;

    private View.OnTouchListener handleTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final AppCompatButton subtract_button = (AppCompatButton) findViewById(R.id.subtract_button);
        final AppCompatButton add_button = (AppCompatButton) findViewById(R.id.add_button);
        final View touch_view = (View) findViewById(R.id.touch_view);

        handleTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isOpened) {
                        touch_view.setVisibility(View.GONE);
                        add_button.animate().translationX(0).setDuration(100).setInterpolator(null);
                        subtract_button.animate().translationX(0).setDuration(200).setInterpolator(null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                add_button.setVisibility(View.GONE);
                                subtract_button.setVisibility(View.GONE);
                            }
                        }, 200);

                    }
                    isOpened = !isOpened;
                }
                return true;
            }
        };

        touch_view.setOnTouchListener(handleTouch);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpened) {
                    touch_view.setVisibility(View.GONE);
                    add_button.animate().translationX(0).setDuration(100).setInterpolator(null);
                    subtract_button.animate().translationX(0).setDuration(200).setInterpolator(null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            add_button.setVisibility(View.GONE);
                            subtract_button.setVisibility(View.GONE);
                        }
                    }, 200);

                } else {
                    touch_view.setVisibility(View.VISIBLE);
                    add_button.setVisibility(View.VISIBLE);
                    subtract_button.setVisibility(View.VISIBLE);
                    add_button.animate().translationX(-340).setDuration(400).setInterpolator(new CustomBounce(0.3, 10));
                    subtract_button.animate().translationX(-170).setDuration(200).setInterpolator(new CustomBounce(0.3, 10));
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


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.animate().translationX(0).setDuration(300);
                } else {
                    fab.animate().translationX(200).setDuration(300);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onStopDrag(RecyclerView.ViewHolder viewHolder) {

    }
}
