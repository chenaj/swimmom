package swim.swimmom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.StringTokenizer;

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
       /* DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        dop.insertStatistics(db, "wajiha", "200 yd. Medley Relay", "00:01:30","3/12/2015", "01");
        dop.insertStatistics(db, "alina", "100 yd. Butterfly", "00:00:01","3/25/2015", "04");
        dop.insertStatistics(db, "wajiha", "200 yd. Freestyle Relay", "05:00:17","4/9/2015", "02");
        dop.insertStatistics(db, "dfg", "50 yd. Freestyle", "00:00:00","4/8/2015", "01");
        dop.insertStatistics(db, "wajiha", "100 yd. Backstroke", "00:00:30","3/2/2015", "01");*/

        sort_spinner = (Spinner) findViewById(R.id.sortSpinner);
        event_spinner = (Spinner) findViewById(R.id.eventSpinner);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Sort", String.valueOf(position));
                sortList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Event", String.valueOf(position));
                sortList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        s_sort = "";
        s_event ="";
        resetForm(getWindow().getDecorView().findViewById(android.R.id.content));
        //populateList();
        sortList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic_swim, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,StatisticActivity.class);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            startActivity(new Intent(this, StatisticActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    public void resetForm(View v)
    {
        //Sort Spinner
        sort_spinner.setPrompt("Date");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sort_spinner.setAdapter(adapter);

        //Event Spinner
        event_spinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.events_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        event_spinner.setAdapter(adapter_1);
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
        Log.d("u selected =", s_event);

        /*if (current_sort == "Time")
        {
            current_sort = "Event_Time";

        }*/
        String event, time, date, opponent;
        Cursor cursor;
        EventList.clear();
        lv = (ListView) findViewById(R.id.meetList);
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name ='" + current_swimmer + "'", null);
        // cursor = db.rawQuery("SELECT * FROM Statistics_TABLE ORDER BY Event_TIme ", null);


        if(s_event.contains("Select...")) {
            //cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name='"+current_swimmer+"' ORDER BY Date DESC ", null);
            new MessagePrinter().longMessage(this, "Please select an event to view");
        }
        else
        {
            if (s_sort.contains("Date")) {
                Log.d("Sort by", "Date");

           /*if(s_event.contains("Select...")) {
               //cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name='"+current_swimmer+"' ORDER BY Date DESC ", null);
               new MessagePrinter().longMessage(this, "Please select an event to view");
           }
           else{*/
                cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name='" + current_swimmer + "' AND  Event = '" + s_event + "' ORDER BY Date ", null);
                //}
            } else {
                Log.d("Sort by", "Time");
                Log.d("s_event", s_event);
                if (s_event.contains("Select...")) {
                    Log.d("Event", "Select...");
                    cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name='" + current_swimmer + "' ORDER BY Event_Time ", null);
                } else {
                    Log.d("Event", "Event");
                    cursor = db.rawQuery("SELECT * FROM Statistics_TABLE WHERE Name='" + current_swimmer + "' AND Event = '" + s_event + "' ORDER BY Event_Time ", null);
                }
            }

            if (cursor.moveToFirst())
            {
                while (!cursor.isAfterLast()) {
                    event = cursor.getString(cursor.getColumnIndex("Event"));
                    time = cursor.getString(cursor.getColumnIndex("Event_Time"));
                    date = cursor.getString(cursor.getColumnIndex("Date"));
                    opponent = cursor.getString(cursor.getColumnIndex("Opponent"));
                    HashMap<String, String> map = new HashMap<String, String>();
                    Log.d("Event", event);
                    Log.d("Time", time);
                    Log.d("Date", date);
                    Log.d("Opponent", opponent);
                    map.put("Event", event);
                    map.put("Time", time);
                    map.put("Date", date);
                    map.put("Opponent", opponent);

                    EventList.add(map);
                    cursor.moveToNext(); // Move to next row retrieved
                }
            }
        }
        if(EventList.size() == 0 && !s_event.contains("Select..."))
            new MessagePrinter().longMessage(this, "No records exist for this event!");
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, EventList, R.layout.statsrow,
                new String[]{"Opponent", "Time", "Date", }, new int[]
                {R.id.Opponent, R.id.Time, R.id.Date});
        lv.setAdapter(simpleAdapter);
    }
//////////////////////for sort////////////
}