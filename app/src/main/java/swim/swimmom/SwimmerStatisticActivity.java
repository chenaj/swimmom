package swim.swimmom;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wajihaf56 on 2/24/2015.
 */


public class SwimmerStatisticActivity extends ActionBarActivity {

    ArrayList<HashMap<String, String>> EventList = new ArrayList<>();
    ListView lv;
    String current_swimmer;
    Spinner sort_spinner;
    Spinner event_spinner;
    String current_sort;
    String selected_event;
    String s_sort, s_event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_swim);
        current_swimmer = StatisticActivity.selected_swimmer;
        new MyActionBar(getSupportActionBar(), "" + current_swimmer + "'s Statistics");
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        dop.insertStatistics(db, "wajiha", "Z123 freestyle", "00:01:30","3/12/2015", "01");
        dop.insertStatistics(db, "alina", "B123 butterfly", "00:00:01","3/25/2015", "04");
        dop.insertStatistics(db, "wajiha", "321 freestyle", "05:00:17","4/9/2015", "02");
        dop.insertStatistics(db, "dfg", "C123 freestyle", "00:00:00","4/8/2015", "01");
        dop.insertStatistics(db, "wajiha", "Zfreestyle", "00:00:30","3/2/2015", "01");
        s_sort = "";
        s_event ="";
        resetForm(getWindow().getDecorView().findViewById(android.R.id.content));
        populateList();
       sortList();
    }
    public void resetForm(View v)
    {


        //Sort Spinner
        sort_spinner = (Spinner) findViewById(R.id.sortSpinner);
        sort_spinner.setPrompt("Date");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sort_spinner.setAdapter(adapter);


        //Event Spinner
        event_spinner = (Spinner) findViewById(R.id.eventSpinner);
        event_spinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.events_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        event_spinner.setAdapter(adapter_1);

        return;
    }

    public void populateList() {
        String event, time, date;
        EventList.clear();
        lv = (ListView) findViewById(R.id.meetList);
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name ='" + current_swimmer + "'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM Statistics_TABLE", null);


        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                event = cursor.getString(cursor.getColumnIndex("Event"));
                time = cursor.getString(cursor.getColumnIndex("Event_Time"));
                date = cursor.getString(cursor.getColumnIndex("Date"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Event", event);
                map.put("Time", time);
                map.put("Date", date);
                EventList.add(map);
                cursor.moveToNext(); // Move to next row retrieved
            }
        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(this, EventList, R.layout.statsrow,
                new String[]{"Event", "Time", "Date"}, new int[]
                {R.id.Event, R.id.Time, R.id.Date});
        lv.setAdapter(simpleAdapter);
    }
    private void sortList ()
    {
        EventList.clear();
        ///get the value
        sort_spinner = (Spinner) findViewById(R.id.sortSpinner);
        event_spinner = (Spinner) findViewById(R.id.eventSpinner);

        //
        s_sort = sort_spinner.getSelectedItem().toString();
        s_event = event_spinner.getSelectedItem().toString();



        Log.d("u selected = ", s_sort);

        /*if (current_sort == "Time")
        {
            current_sort = "Event_Time";

        }*/

            String event, time, date;
            Cursor cursor;
            EventList.clear();
            lv = (ListView) findViewById(R.id.meetList);
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();
            //Cursor cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name ='" + current_swimmer + "'", null);

       if (s_sort == "Date") {

           if(s_event == "Select...") {
               cursor = db.rawQuery("SELECT * FROM Statistics_TABLE ORDER BY Date ", null);
           }
           else{
               cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Event = '"+s_event+"' ORDER BY Date ", null);
           }
       }
        else {
           if (s_event == "Select...") {
               cursor = db.rawQuery("SELECT * FROM Statistics_TABLE ORDER BY Event_TIme ", null);
           }
           else {
               cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Event = '"+s_event+"' ORDER BY Event_TIme ", null);
           }
       }




      if (cursor.moveToFirst())
           {
                while (cursor.isAfterLast() == false) {
                    event = cursor.getString(cursor.getColumnIndex("Event"));
                    time = cursor.getString(cursor.getColumnIndex("Event_Time"));
                    date = cursor.getString(cursor.getColumnIndex("Date"));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Event", event);
                    map.put("Time", time);
                    map.put("Date", date);
                    EventList.add(map);
                    cursor.moveToNext(); // Move to next row retrieved
                }
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, EventList, R.layout.statsrow,
                    new String[]{"Event", "Time", "Date"}, new int[]
                    {R.id.Event, R.id.Time, R.id.Date});
            lv.setAdapter(simpleAdapter);





    }
//////////////////////for sort////////////

}