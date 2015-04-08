package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
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

    public static String chosenMeetId;
    ArrayList<HashMap<String,String>> swimmerList = new ArrayList(); //list of swimmers swimming in current event Keys (name, time)
    int eventIndex=0;
    ListView lv;
    String swimmer;
    ArrayList<HashMap<String,String>> participants = new ArrayList(); // Stores list of swimmer names participating in the meet Keys(name, event)
    public static ArrayList<HashMap<String,String>> finalStats = new ArrayList(); // Stores times of swimmers for all events Keys(Name,event,time)

    List<String> eventList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_swimmers_events);

        chosenMeetId = MeetActivity.chosenMeetId;
        participants.clear();
        swimmerList.clear();
        finalStats.clear();
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
        alert.setCancelable(true);

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setImeOptions(EditorInfo.IME_ACTION_NONE);
        input.setHint("00:00.00");
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(6);
        input.setFilters(FilterArray);
        alert.setView(input);

        /*
        final boolean[] overridingText = {false};
        final int[] count = {1};
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (overridingText[0])
                    return;

                if(count[0] == 4)
                {
                    overridingText[0] = true;
                    //input.setSelection(0);
                    input.setText(editable.toString()+":");

                    overridingText[0] = false;
                }
                else if(count[0] == 2)
                {
                    overridingText[0] = true;
                    input.setText(editable.toString()+".");
                    //input.setSelection(3);
                    overridingText[0] = false;
                }
                count[0]++;
            }
        });
        */

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

    private AlertDialog cancelMeetDialog(final Context context)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Exit Meet")
                .setMessage("Are you sure you would like to cancel this meet?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//save meet
                        startActivity(new Intent(context, MeetActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//cancel
                        dialog.dismiss();
                    }
                })
                .create();
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
            while (!cursor.isAfterLast())
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                event = cursor.getString(cursor.getColumnIndex("Event"));

                Log.d("Name: ",name);
                Log.d("Event: ",event);
                HashMap<String,String> map= new HashMap<>();
                map.put("Name", name);
                map.put("Event",event);

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
        if(eventIndex<eventList.size()-1)
        {
            //make sure all times are filled in before advancing to next event
            for (int i=0; i<swimmerList.size();i++)
            {
                if(swimmerList.get(i).get("Time").equals("00:00.00"))
                {
                    new MessagePrinter().shortMessage(this, "Please fill in empty times");
                    return;
                }
            }

            String name, time;
            for (int i=0; i<swimmerList.size();i++)
            {
                name=swimmerList.get(i).get("Name");
                time=swimmerList.get(i).get("Time");

                HashMap<String,String> map= new HashMap<>();
                map.put("Name",name ); // Add swimmer name to eventList
                map.put("Event", eventList.get(eventIndex)); // Add swimmer name to eventList
                map.put("Time", time); // Add event to swimmerList
                finalStats.add(map);
            }

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
