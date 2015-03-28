package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class MeetFinishActivity extends ActionBarActivity {
    public static ArrayList<HashMap<String,String>> finalStats;
    String meetDate;
    String meetID;
    String meetOpponent;
    ListView lv;
    String swimmer, event, time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_finish);
        new MyActionBar(getSupportActionBar(), "Meet Complete"); // Create action bar

        finalStats = MeetSwimmersEvents.finalStats;
        meetDate = MeetActivity.chosenMeetDate;
        meetID = MeetActivity.chosenMeetId;
        meetOpponent = MeetActivity.chosenMeetOpponent;

        Log.d("Meet Date", meetDate);
        Log.d("Meet ID", meetID);
        Log.d("Meet Opponent", meetOpponent);

        lv = (ListView) findViewById(R.id.swimmerList);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, finalStats, R.layout.finalstatsrow,
                new String[]{"Name", "Event", "Time"}, new int[]{R.id.name, R.id.event, R.id.eventTime});
        lv.setAdapter(simpleAdapter); // Apply the adapter to the list view

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                swimmer = finalStats.get(position).get("Name");
                event = finalStats.get(position).get("Event");
                time = finalStats.get(position).get("Time");
                Log.d("You clicked on" , swimmer);
                new RumbleAction(view);
                showKeypad(getApplicationContext(),view, position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_finish, menu);
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

    private void showKeypad (final Context context, final View view, final int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(event);
        alert.setMessage(swimmer);
        alert.setCancelable(true);

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setImeOptions(EditorInfo.IME_ACTION_NONE);
        input.setHint(time);
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
                map.put("Event", event);
                map.put("Time", value ); // Add event to swimmerList
                finalStats.set(position, map);
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

    private AlertDialog saveMeetDialog(final Context context, final View view)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Save Meet")
                .setMessage("Would you like to save this meet?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//save meet
                        saveStats();
                        startActivity(new Intent(context, MeetActivity.class));
                        new RumbleAction(view);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//cancel
                        dialog.dismiss();
                        new RumbleAction(view);
                    }
                })
                .create();
    }

    public void saveStats()
    {
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();

        for (int i=0; i<finalStats.size();i++)
        {
            Log.d("Name", finalStats.get(i).get("Name"));
            Log.d("Event", finalStats.get(i).get("Event"));
            Log.d("Time", finalStats.get(i).get("Time"));

            dop.insertStatistics(db, finalStats.get(i).get("Name"), finalStats.get(i).get("Event"),
                    finalStats.get(i).get("Time"), meetDate, meetID, meetOpponent);
        }
        //remove meet from list after completing it
        db.execSQL("DELETE FROM Meet_TABLE WHERE Meet_Id='"+MeetActivity.chosenMeetId+"'");
    }

    public void saveMeet(View v)
    {
        AlertDialog ad = saveMeetDialog(this,v);
        ad.show();
    }
}
