package yamaken1343.spvideomotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    public static final String VIDEOFQDN = "yamaken1343.spvideomotion.VIDEOFQDN";
    Button startButton;
    String videoUrl;
    String videoId;
    String videoFqdn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.url_field);
                videoUrl = et.getText().toString();

                Spinner sp = findViewById(R.id.select_id);
                videoId = (String)sp.getSelectedItem();

                videoFqdn = videoUrl+videoId;

                Intent intent = new Intent(MainActivity.this, VideoPlay.class);
                intent.putExtra(VIDEOFQDN, videoFqdn);
                startActivity(intent);




            }
        });
    }
}
