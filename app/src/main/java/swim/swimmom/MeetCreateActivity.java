package swim.swimmom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MeetCreateActivity extends ActionBarActivity {

    TextView displayTime;
    EditText pickTime;

    int pHour;
    int pMinute;
    String timeOfDay = " A.M.";
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    final int TIME_DIALOG_ID = 0;

    /** Callback received when the user "picks" a time in the dialog */
    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    pHour = hourOfDay;
                    pMinute = minute;
                    if(hourOfDay > 12)
                    {
                        pHour = hourOfDay - 12;
                        timeOfDay = " P.M.";
                    }
                    updateDisplay();
                }
            };

    private void updateDisplay() {
        displayTime.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)).append(timeOfDay));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        displayTime = (TextView) findViewById(R.id.timePicker);
        pickTime = (EditText) findViewById(R.id.timePicker);
        pickTime.setInputType(0);

        // Listener for click event of the button
        pickTime.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);

            }
        });
    }


    private static String pad(int c) // Add padding to numbers less than ten
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(new Intent(this, MeetActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToMeets(View v) //navigate back to profile page when save is pressed
    {
        new MessagePrinter().shortMessage(this, "Meet Created!");
        startActivity(new Intent(this, MeetActivity.class));
    }
}
