package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private SeekBar positionBar;
    private SeekBar volumeBar;
    private TextView elapsedTimeLabel;
    private TextView remainingTimeLabel;
    private MediaPlayer mp;
    private int totalTime;
    private int currrentMusic = 1;
    private int[] a = new int[4];
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        for (int i = 0; i < a.length; i++) {
            a[i] = getResources().getIdentifier("music" + i, "raw", getPackageName());
        }

        playBtn = findViewById(R.id.playBtn);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
//音楽フォルダのセット
        mp = MediaPlayer.create(this,a[0]);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(3f, 3f);
        totalTime = mp.getDuration();
//positionBarr
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


//volumeBar
        volumeBar =

                findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    //時間の計算
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;

            positionBar.setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = "- " + createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText(remainingTime);

            return true;
        }
    });

    //時間
    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }


    public void playBtnClick(View view) {
        if (!mp.isPlaying()) {
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        } else {
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    public void nextBtnClick(View view) {
        currrentMusic += 1;
        if (currrentMusic >= 4) {
            currrentMusic = 0;
        }
        mp.stop();
        mp = MediaPlayer.create(this, a[currrentMusic]);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(3f, 3f);
        totalTime = mp.getDuration();
        mp.start();
    }

    //    a[i] = getResources().getIdentifier("music" + i, "raw", getPackageName());
    public void reverseBtnClick(View view) {
        currrentMusic -= 1;
        if (currrentMusic <= -1) {
            currrentMusic = 3;
        }
        mp.stop();
        mp = MediaPlayer.create(this, a[currrentMusic]);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(3f, 3f);
        totalTime = mp.getDuration();
        mp.start();
    }

    public void go2List(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

}
//外部ストレージを読み込む
//if (isExternalStorageReadable()) {
// FileInputStream inputStream;
// byte[] buffer = new byte[256];
//
// // ※以下、例外処理は省略
//
// File file = new File(getExternalFilesDir(null), "myfile.txt");
//
// if (file.exists()) {
//  inputStream = new FileInputStream(file);
//  inputStream.read(buffer);
//
//  String data = new String(buffer, "UTF-8");
//
//  inputStream.close();
// }
//}