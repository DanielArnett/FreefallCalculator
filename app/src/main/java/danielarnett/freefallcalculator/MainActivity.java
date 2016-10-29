package danielarnett.freefallcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static String HEADING_MESSAGE = "Heading";
    public static String SPEED_MESSAGE = "Speed";
    public static String TIME_MESSAGE = "Time";
    public static String DISTANCE_MESSAGE = "Distance";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    public double getValueOf(int id) {
        String text = ((EditText) findViewById(id)).getText().toString();
        if (text.compareTo("") == 0) {
            return -1;
        }
        return Double.parseDouble(text);
    }

    public void calculateFreeFall(View view) {
        double altitudes[] = new double[5];
        double speeds[] = new double[5];
        double headings[] = new double[5];
        altitudes[0] = getValueOf(R.id.altitudeText1);
        altitudes[1] = getValueOf(R.id.altitudeText2);
        altitudes[2] = getValueOf(R.id.altitudeText3);
        altitudes[3] = getValueOf(R.id.altitudeText4);
        altitudes[4] = getValueOf(R.id.altitudeText5);
        speeds[0]    = getValueOf(R.id.speedText1);
        speeds[1]    = getValueOf(R.id.speedText2);
        speeds[2]    = getValueOf(R.id.speedText3);
        speeds[3]    = getValueOf(R.id.speedText4);
        speeds[4]    = getValueOf(R.id.speedText5);
        headings[0] = getValueOf(R.id.headingText1);
        headings[1] = getValueOf(R.id.headingText2);
        headings[2] = getValueOf(R.id.headingText3);
        headings[3] = getValueOf(R.id.headingText4);
        headings[4] = getValueOf(R.id.headingText5);
        double exitAltitude = getValueOf(R.id.exitText);
        double deployAltitude = getValueOf(R.id.deployText);
        FreeFallCalculator freeFallCalculator = new FreeFallCalculator(exitAltitude, deployAltitude);
        for (int i = 0; i < altitudes.length; i++) {
            freeFallCalculator.addWind(altitudes[i], speeds[i], headings[i]);
        }
        double heading = freeFallCalculator.winds.getAverageHeading();
        Log.d("FreeFallCalculator", "Average Wind Heading in Degrees:        " +
                String.valueOf(heading));

        double speed = freeFallCalculator.winds.getAverageWindspeedInRange(
                deployAltitude, exitAltitude);
        Log.d("FreeFallCalculator", "Average Wind Speed in Miles/Hour:       " +
                String.valueOf(speed));

        double time = freeFallCalculator.getFreefallTimeInSeconds();
        Log.d("FreeFallCalculator", "Approximate Time in Freefall:           " +
                String.valueOf(time));

        double distance = freeFallCalculator.getHorizontalDistanceTraveled();
        Log.d("FreeFallCalculator", "Distance Traveled Horizontally in Feet: " +
                String.valueOf(distance));


        Intent intent = new Intent(this, FreeFallResultsActivity.class);
        String message = "";
        intent.putExtra("Yo", message);
        intent.putExtra(HEADING_MESSAGE, heading);
        intent.putExtra(SPEED_MESSAGE, speed);
        intent.putExtra(TIME_MESSAGE, time);
        intent.putExtra(DISTANCE_MESSAGE, distance);
        startActivity(intent);
    }
}
