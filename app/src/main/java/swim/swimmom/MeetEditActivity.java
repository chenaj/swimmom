package swim.swimmom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MeetEditActivity extends ActionBarActivity {

    EditText oppField, locField, dateField, timeField;
    String chosenMeetId;
    String opponent, location, date, time;

    int Year, meetYear;
    int Month, meetMonth;
    int Day, meetDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_edit);
        new MyActionBar(getSupportActionBar(), "Edit Meet"); // Create action bar

        chosenMeetId = MeetActivity.chosenMeetId;
        opponent = MeetActivity.chosenMeetOpponent;
        date = MeetActivity.chosenMeetDate;
        populatePage();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MainActivity.class);
        return super.onOptionsItemSelected(item);
    }

    void populatePage()
    {
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Meet_TABLE WHERE Meet_Id='"+chosenMeetId+"'",null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                //get rest of selected user info
                time = cursor.getString(cursor.getColumnIndex("Time"));
                location = cursor.getString(cursor.getColumnIndex("Location"));
                cursor.moveToNext(); // Move to next row retrieved
            }
        }

        oppField = (EditText) findViewById(R.id.opponent);
        locField = (EditText) findViewById(R.id.location);
        dateField = (EditText) findViewById(R.id.date);
        timeField = (EditText) findViewById(R.id.time);

        oppField.setText(opponent);
        locField.setText(location);
        dateField.setText(date);
        timeField.setText(time);
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
}
