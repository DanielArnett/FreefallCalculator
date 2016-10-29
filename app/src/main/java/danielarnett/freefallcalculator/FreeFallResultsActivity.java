package danielarnett.freefallcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FreeFallResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall_results);
        Intent intent = getIntent();
        double heading = intent.getDoubleExtra(MainActivity.HEADING_MESSAGE, -1);
        double speed = intent.getDoubleExtra(MainActivity.SPEED_MESSAGE, -1);
        double time = intent.getDoubleExtra(MainActivity.TIME_MESSAGE, -1);
        double distance = intent.getDoubleExtra(MainActivity.DISTANCE_MESSAGE, -1);
        TextView headingView = new TextView(this);
        headingView.setTextSize(40);
        headingView.setText("Wind Heading: " + String.valueOf(heading) + "degrees");
        TextView speedView = new TextView(this);
        speedView.setTextSize(40);
        speedView.setText("Wind Speed: " + String.valueOf(speed) + "mph");
        TextView timeView = new TextView(this);
        timeView.setTextSize(40);
        timeView.setText("Freefall Time: " + String.valueOf(time) + "s");
        TextView distanceView = new TextView(this);
        distanceView.setTextSize(40);
        distanceView.setText("Horizontal Travel: " + String.valueOf(distance) + "ft");

        LinearLayout layout = (LinearLayout) findViewById(R.id.resultsLayout);

        layout.addView(headingView);
        layout.addView(speedView);
        layout.addView(timeView);
        layout.addView(distanceView);
    }

}
