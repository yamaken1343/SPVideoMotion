package yamaken1343.spvideomotion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlay extends AppCompatActivity {
    VideoView videoView;
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
        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoFqdn));
        videoView.setMediaController(new MediaController(this));

    }




}
