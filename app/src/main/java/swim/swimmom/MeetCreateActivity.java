package swim.swimmom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class MeetCreateActivity extends ActionBarActivity{

    String errorMsg = "";
    public static String opponent = "";
    public static String location = "";
    public static String time = "";
    public static String date = "";
    int Year, meetYear;
    int Month, meetMonth;
    int Day, meetDay;


    EditText dateField, locationField, timeField, opponentField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create);
        new MyActionBar(getSupportActionBar(), "Create a Meet"); // Create action bar

        opponentField = (EditText) findViewById(R.id.opponent);
        locationField = (EditText) findViewById(R.id.location);
        dateField =  (EditText) findViewById(R.id.date);
        timeField = (EditText) findViewById(R.id.time);

        // pre-populate fields from previous saved info or start fresh
        opponentField.setText(MeetInfo.opponent);
        locationField.setText(MeetInfo.location);
        dateField.setText(MeetInfo.date);
        timeField.setText(MeetInfo.time);

        // when date field is clicked
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        // when time field is clicked
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void showDatePicker()
    {
        // get current date
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        meetYear = year;
                        meetMonth = monthOfYear;
                        meetDay = dayOfMonth;
                        dateField.setText((monthOfYear + 1) + "/"
                                + dayOfMonth + "/" + year);
                    }
                }, Year, Month, Day);
        dpd.show();
    }

    private void showTimePicker()
    {
        // get current time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        formatTime(hourOfDay, minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }

    private void formatTime(int hourOfDay, int minute) //formats time (i.e., A.M or P.M.)
    {
        String format = "A.M.";
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        }
        timeField.setText(hourOfDay + ":" + pad(minute) + " " + format);
    }

    private static String pad(int c) // Add padding to numbers less than ten
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
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
        new MenuOptions().MenuOption(curView,item,this,MeetActivity.class);
        return super.onOptionsItemSelected(item);

    }

    public void resetForm(View v)
    {
        opponentField = (EditText) findViewById(R.id.opponent);
        locationField = (EditText) findViewById(R.id.location);
        dateField =  (EditText) findViewById(R.id.date);
        timeField = (EditText) findViewById(R.id.time);

        opponentField.setText("");
        locationField.setText("");
        dateField.setText("");
        timeField.setText("");
    }

    public void goToSelectSwimmers(View v) //navigate to select swimmers
    {
        opponentField = (EditText) findViewById(R.id.opponent);
        locationField = (EditText) findViewById(R.id.location);
        dateField =  (EditText) findViewById(R.id.date);
        timeField = (EditText) findViewById(R.id.time);

        opponent = opponentField.getText().toString();
        location = locationField.getText().toString();
        date =  dateField.getText().toString();
        time = timeField.getText().toString();

        // check if fields are filled in correctly
        if (validInput()) {
            MeetInfo.opponent = opponent;
            MeetInfo.location = location;
            MeetInfo.date = date;
            MeetInfo.time = time;
            startActivity(new Intent(this, MeetCreateSwimmersActivity.class));
        } else {
            new MessagePrinter().longMessage(this, errorMsg);
            errorMsg = "";
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

    public boolean validDate()
    {
        //if date of meet is either today or in the future
        if(meetYear >= Year && meetMonth >= Month && meetDay >= Day) {
            return true;
        }
        else
        {
            addNewLine();
            errorMsg += "Please enter a date that has not passed";
            return false;
        }
    }

    public void addNewLine() //adds new line to toast message
    {
        if (errorMsg.length() > 0)
            errorMsg += "\r\n";
    }
}
