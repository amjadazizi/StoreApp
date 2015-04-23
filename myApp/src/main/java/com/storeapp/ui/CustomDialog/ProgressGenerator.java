package com.storeapp.ui.CustomDialog;

/**
 * Created by Amjad on 16-03-2015.
 */
import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

public class ProgressGenerator {

    boolean progressRunnning;

    public interface OnCompleteListener {

        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void stop(){
        progressRunnning = false;
    }

    public void start(final ProcessButton button) {
        progressRunnning = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if(!progressRunnning){
                    mListener.onComplete();
                    return;
                }

                mProgress += 1;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());
    }



    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}
