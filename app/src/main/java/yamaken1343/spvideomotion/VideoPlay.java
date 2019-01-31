package yamaken1343.spvideomotion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class VideoPlay extends AppCompatActivity {
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

        TextView mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setText(videoFqdn);

    }




}
