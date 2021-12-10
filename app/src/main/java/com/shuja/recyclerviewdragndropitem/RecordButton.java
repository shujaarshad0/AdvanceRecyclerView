package com.shuja.recyclerviewdragndropitem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RecordButton extends AppCompatImageView implements View.OnTouchListener{

    private ScaleAnim scaleAnim;
    private boolean listenForRecord = true;
    private int commId;
    private MediaRecorder recorder = null;
    private boolean isRecording;

    private static SoundPool soundPool = null;
    private static int soundStart,soundEnd;


    public RecordButton(Context context, int id) {
        super(context);
        init(context, null);
        commId=id;
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.drawable.ic_profile);

            Drawable imageResource = context.getResources().getDrawable(R.drawable.ic_profile);
            setImageDrawable(imageResource);
        }

        if (soundPool==null){
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .build();
            //these are just two wav files with short intro and exit sounds
            soundStart=soundPool.load(getContext(),R.raw.bear,0);
            soundEnd=soundPool.load(getContext(),R.raw.bear,0);
        }

        scaleAnim = new ScaleAnim(this);
        this.setOnTouchListener(this);
    }

    private void setTheImageResource(int imageResource) {
        Drawable image = AppCompatResources.getDrawable(getContext(), imageResource);
        setImageDrawable(image);
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
        if (isListenForRecord()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((RecordButton) v).startScale();
                    soundPool.play(soundStart,1,1,0,0,1);
//                    startRecording();
                    break;

                case MotionEvent.ACTION_UP:
//                    stopRecording();
                    soundPool.play(soundEnd,1,1,0,0,1);
                    ((RecordButton) v).stopScale();
                    break;
            }

        }
        return isListenForRecord();
    }

    File file;
    //audio recording tools
    private void startRecording() {
        if (isRecording){
            stopRecording();
        }
        String fileName=getContext().getExternalCacheDir().getAbsolutePath()+"/"+commId+"_"+new Date().getTime()+".amr";
        file=new File(fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        recorder.setAudioSamplingRate(8000);
        recorder.setAudioChannels(1);
        recorder.setAudioEncodingBitRate(12000);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(),fileName, Toast.LENGTH_LONG).show();
        recorder.start();

        setIsRecording(true);
    }

    private void stopRecording() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            setIsRecording(false);
            recordingFinishedListener.onRecordingFinished(file);
            recorder = null;
        }
    }

    private void setIsRecording(boolean isRecording){
        this.isRecording=isRecording;
    }


    protected void startScale() {
        scaleAnim.start();
    }

    protected void stopScale() {
        scaleAnim.stop();
    }

    public void setListenForRecord(boolean listenForRecord) {
        this.listenForRecord = listenForRecord;
    }

    public boolean isListenForRecord() {
        return listenForRecord;
    }

    //callback for when a recording has been made
    public interface RecordingFinishedListener{
        void onRecordingFinished(File file);
    }

    RecordingFinishedListener recordingFinishedListener;

    public void setRecordingFinishedListener(RecordingFinishedListener recordingFinishedListener) {
        this.recordingFinishedListener = recordingFinishedListener;
    }
}
