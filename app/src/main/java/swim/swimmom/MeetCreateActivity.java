package swim.swimmom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    String errorMsg = "";

    public static String opponent = "";
    public static String location = "";
    public static String time = "";
    public static String date = "";
    EditText dateField, locationField, timeField, opponentField;

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
        new MyActionBar(getSupportActionBar(), "Create a Meet"); // Create action bar

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
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MeetCreateActivity.class,MeetActivity.class);
        return super.onOptionsItemSelected(item);

    }

    public void goToSelectSwimmers(View v) //navigate to select swimmers
    {
        opponentField = (EditText) findViewById(R.id.opponent);
        locationField = (EditText) findViewById(R.id.location);
        dateField =  (EditText) findViewById(R.id.date);
        timeField = (EditText) findViewById(R.id.timePicker);

        opponent = opponentField.getText().toString();
        location = locationField.getText().toString();
        date =  dateField.getText().toString();
        time = timeField.getText().toString();

        // check if fields are filled in correctly
        if(validInput()) {
            startActivity(new Intent(this, MeetCreateSwimmersActivity.class));
        }else {
            new MessagePrinter().longMessage(this, errorMsg);
            errorMsg = "";
            return;
        }
    }

    public boolean validInput()
    {
        boolean validInput = true;
        if(opponent.length() < 1) {
            addNewLine();
            errorMsg += "Please enter an opponent";
            validInput = false;
        }
        if(location.length() < 1) {
            addNewLine();
            errorMsg += "Please enter a location";
            validInput = false;
        }
        if(date.length() < 1) {
            addNewLine();
            errorMsg += "Please enter a date";
            validInput = false;
        }
        if(time.length() < 1) {
            addNewLine();
            errorMsg += "Please enter a time";
            validInput = false;
        }
        return validInput;
    }

    public void addNewLine() //adds new line to toast message
    {
        if (errorMsg.length() > 0)
            errorMsg += "\r\n";
    }
}
