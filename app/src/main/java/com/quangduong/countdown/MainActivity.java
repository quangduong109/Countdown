package com.quangduong.countdown;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quangduong.countdown.utils.AudioPlayer;
import com.quangduong.countdown.utils.GifDecoderView;
import com.quangduong.countdown.utils.GifWebView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CountDownTimer mCountDownTimer;
    long mInitialTime = DateUtils.DAY_IN_MILLIS * 0 +
            DateUtils.HOUR_IN_MILLIS * 0 +
            DateUtils.MINUTE_IN_MILLIS * 0 +
            DateUtils.SECOND_IN_MILLIS * 5;
    MediaPlayer mp = new MediaPlayer();

    TextView mTv_days;
    TextView mTv_time;
    Button mBtn_merryChristmas;
    Button mBtn_reset;
    GifDecoderView progressbar;
    GifDecoderView textGif;
    RelativeLayout rl;
    RelativeLayout.LayoutParams lp;
    int layoutWidth = 0;
    float delta = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout) findViewById(R.id.main_layout);

        InputStream stream = null;
        try {
            stream = getAssets().open("progressbar.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        progressbar = new GifDecoderView(this, stream);

        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rl.addView(progressbar, lp);
        progressbar.getLayoutParams().height = 300;
        progressbar.getLayoutParams().width = 300;

        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                // Ensure you call it only once :
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                layoutWidth = rl.getWidth();// width must be declare as a field
            }
        });

        mInitialTime = getTimeToChristmas();

        AssetManager am = getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Kingthings Christmas 2.2.ttf"));

        mTv_days = (TextView) findViewById(R.id.tv_days);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mBtn_merryChristmas = (Button) findViewById(R.id.btn_merryChristmas);
        mBtn_reset = (Button) findViewById(R.id.btn_reset);

        mBtn_merryChristmas.setOnClickListener(this);
        mBtn_reset.setOnClickListener(this);

        mTv_days.setTypeface(typeface);
        mTv_time.setTypeface(typeface);
        mBtn_merryChristmas.setTypeface(typeface);
        mBtn_reset.setTypeface(typeface);

        mCountDownTimer = new CountDownTimerExt(mInitialTime, 1000).start();
    }

    private long getTimeToChristmas() {
        Calendar calendar = Calendar.getInstance();
        long startTime = calendar.getTimeInMillis();
        calendar.set(2015, 11, 24, 0, 0, 0);
        long endTime = calendar.getTimeInMillis();
        return endTime - startTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_merryChristmas:
                mCountDownTimer.cancel();
                long mInitialTime1 = DateUtils.DAY_IN_MILLIS * 0 +
                        DateUtils.HOUR_IN_MILLIS * 0 +
                        DateUtils.MINUTE_IN_MILLIS * 0 +
                        DateUtils.SECOND_IN_MILLIS * 15;
                mCountDownTimer = new CountDownTimerExt(mInitialTime1, 1000).start();
                AudioPlayer.playFromAssert(mp, getAssets(), "audios/countdown.mp3");
                break;
            case R.id.btn_reset:
                rl.removeView(textGif);
                mCountDownTimer.cancel();
                try{
                    if(mp != null){
                        if(mp.isPlaying()){
                            mp.stop();
                        }
                        mp.reset();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                mInitialTime = getTimeToChristmas();
                mCountDownTimer = new CountDownTimerExt(mInitialTime, 1000).start();
        }


    }

    public class CountDownTimerExt extends CountDownTimer {


        StringBuilder time = new StringBuilder();

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountDownTimerExt(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mTv_time.setText(DateUtils.formatElapsedTime(0));
            AudioPlayer.playFromAssert(mp, getAssets(), "audios/music.mp3");

            InputStream stream = null;
            try {
                stream = getAssets().open("text.gif");
            } catch (IOException e) {
                e.printStackTrace();
            }

            textGif = new GifDecoderView(getBaseContext(), stream);

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rl.addView(textGif, lp1);
            textGif.getLayoutParams().height = 500;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            time.setLength(0);
            float dayUtilFinish =(millisUntilFinished / DateUtils.DAY_IN_MILLIS);
            float days = (mInitialTime / (1000*60*60*24));
            if(days > 0){
                delta = dayUtilFinish/days;
            }else {
                delta = 0;
            }
            // Use days if appropriate
            if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                if(count > 1)
                    time.append(count).append(" days ");
                else
                    time.append(count).append(" day ");

                millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
            }
            mTv_days.setText(time.toString());
            mTv_time.setText(DateUtils.formatElapsedTime(Math.round((float) millisUntilFinished / 1000.0f)));
            int margin = (int) (layoutWidth - delta*layoutWidth );
            lp.setMargins( margin < layoutWidth - 400 ? margin : layoutWidth - 400, 0, 0, 0);
            progressbar.setLayoutParams(lp);
        }
    }
}
