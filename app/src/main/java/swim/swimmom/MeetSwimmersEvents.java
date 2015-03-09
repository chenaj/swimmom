package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MeetSwimmersEvents extends ActionBarActivity {

    String chosenMeetId=MeetActivity.chosenMeetId;
    ArrayList<HashMap<String,String>> swimmerList = new ArrayList();
    int eventIndex=0;
    ListView lv;
    String swimmer;
    ArrayList<HashMap<String,String>> participants = new ArrayList(); // Stores list of swimmer names participating in the meet

    List<String> eventList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_swimmers_events);

        getParticipants();

        String[] eventRes=getResources().getStringArray(R.array.events_array2);
        eventList= Arrays.asList(eventRes);
        populateList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                swimmer = swimmerList.get(position).get("Name");


                Log.d("You clicked on" , swimmer);
                new RumbleAction(view);
                showKeypad(getApplicationContext(),view, position);
            }
        });


    }

    private void showKeypad (final Context context, final View view, final int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Time Input");
        alert.setMessage(swimmer);

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(6);
        input.setFilters(FilterArray);
        alert.setView(input);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something with value!
                String value = input.getText().toString();
                Log.d("Value before", value);
                value = formatTime(value);
                Log.d("Value after", value);

                HashMap<String,String> map= new HashMap<>();
                map.put("Name", swimmer); // Add swimmer name to eventList
                map.put("Time", value ); // Add event to swimmerList
                swimmerList.set(position, map);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        alert.show();
    }

    public String formatTime(String value)
    {
        StringBuilder tempstr = new StringBuilder("00:00.00");
        int vIndex = value.length()-1;
        for(int i=7; vIndex > -1; i--)
        {
            if(i != 5 && i!= 2)
            {
                tempstr.setCharAt(i,value.charAt(vIndex));
                vIndex--;
            }
        }

        return tempstr.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_swimmers_events, menu);

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

    public void getParticipants()
    {
        participants.clear();
        String name, event;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM Participants_TABLE WHERE Meet_Id='"+chosenMeetId+"'",null);

        String query = "SELECT * FROM Participants_TABLE WHERE Meet_Id='"+chosenMeetId+"'";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst())
        {
            Log.d("fzdffdzfzsfzfdzfzxf: ","");
            while (!cursor.isAfterLast())
            {

                name = cursor.getString(cursor.getColumnIndex("Name"));
                event = cursor.getString(cursor.getColumnIndex("Event"));

                Log.d("Name: ",name);
                Log.d("Event: ",event);
                HashMap<String,String> map= new HashMap<>();
                map.put("Name", name); // Add swimmer name to eventList
                map.put("Event",event); // Add event to swimmerList

                participants.add(map);

                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor.close();

        Log.d("Size of Participants","" + participants.size());

    }

    public void populateList() {

        new MyActionBar(getSupportActionBar(), eventList.get(eventIndex)); // Create action bar
        String eventName=eventList.get(eventIndex);
        swimmerList.clear();
        for(int row=0;row<participants.size();row++) {
            if(participants.get(row).get("Event").equals(eventName)) {

                String name = participants.get(row).get("Name");
                HashMap<String,String> map= new HashMap<>();
                map.put("Name", name); // Add swimmer name to eventList
                map.put("Time", "00:00.00"); // Add event to swimmerList
                swimmerList.add(map);
            }
        }

        if(swimmerList.size()!=0) {
            lv = (ListView) findViewById(R.id.eventList);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, swimmerList, R.layout.eventrow,
                    new String[]{"Name", "Time"}, new int[]{R.id.name, R.id.eventTime});
            lv.setAdapter(simpleAdapter); // Apply the adapter to the list view
        }
        else
            goToNextEvent();
    }

    public void goToNextEvent(View v){
        if(eventIndex<eventList.size()-1) {
            eventIndex++;
            populateList();
        }
        else
            startActivity(new Intent(this, MeetFinishActivity.class));


    }

    public void goToNextEvent(){
        if(eventIndex<eventList.size()-1) {
            eventIndex++;
            populateList();
        }
        else
            startActivity(new Intent(this, MeetFinishActivity.class));
    }

}
