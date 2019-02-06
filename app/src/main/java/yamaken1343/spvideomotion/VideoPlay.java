package yamaken1343.spvideomotion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class VideoPlay extends AppCompatActivity implements SensorEventListener {
    VideoView videoView;
    SensorManager sm;
    Sensor accelerometer;
    Sensor gyroscope;

    private float[] accValue = new float[3];
    private float[] gyroValue = new float[3];

    boolean fileOpened = false;
    BufferedWriter bw;
    FileOutputStream fos;

    int isTouch;
    boolean isPlay = false;
    boolean isGetAcc = false;
    boolean isGetGyro = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        String videoFqdn = intent.getStringExtra(MainActivity.VIDEOFQDN);
        String videoID = intent.getStringExtra(MainActivity.VIDEOID);
        String userName = intent.getStringExtra(MainActivity.USERNAME);

        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoFqdn));
//        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
                isPlay = true;
            }
        });

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        String dirFilepath = Environment.getExternalStorageDirectory().getPath() + "/SPVM/" + userName;

        File dir = new File(dirFilepath);
        boolean makedir = dir.mkdirs();

        String fullFilepath;
        fullFilepath = dirFilepath + "/" + videoID + ".csv";
        File file = new File(fullFilepath);

        try {
            fos = new FileOutputStream(file, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            bw = new BufferedWriter(osw);
            fileOpened = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accValue = event.values.clone();
            isGetAcc = true;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroValue = event.values.clone();
            isGetGyro = true;
        }
        if (fileOpened && isPlay && isGetAcc && isGetGyro) {
            try {
                bw.write(String.format(Locale.ENGLISH, "%d, %f, %f, %f, %f, %f, %f, %d\n",
                        System.currentTimeMillis(), accValue[0], accValue[1], accValue[2], gyroValue[0], gyroValue[1], gyroValue[2], isTouch));
                bw.flush();
                isGetAcc = false;
                isGetGyro = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        if (fileOpened) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileOpened = false;
        videoView.stopPlayback();
        isPlay = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = 1;
                break;
            case MotionEvent.ACTION_UP:
                isTouch = 0;
                break;
        }
        return true;
    }

}
