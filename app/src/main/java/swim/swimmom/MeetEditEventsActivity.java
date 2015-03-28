package swim.swimmom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MeetEditEventsActivity extends ActionBarActivity {

    ListView lv;
    ArrayList selectedSwimmers = new ArrayList();
    TextView sName;
    int swimmerIndex = 0;
    String currentSwimmer;
    ArrayList selectedEvents = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_edit_events);
        new MyActionBar(getSupportActionBar(), "Select Events"); // Create action bar

        //populate array with list of all events
        MeetInfo.swimmers.clear();
        populateList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                String event = lv.getItemAtPosition(position).toString();
                if(selectedEvents.contains(event)) //if event is
                    selectedEvents.remove(event);
                else
                    selectedEvents.add(event);
                new RumbleAction(view);
                view.showContextMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_edit_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item, this, MeetEditSwimmersActivity.class);
        return super.onOptionsItemSelected(item);
    }


    public void populateList()
    {
        selectedSwimmers = MeetEditSwimmersActivity.selectedSwimmers; // Stores list of swimmer names
        //**Get list of selected swimmers and add events list under each swimmer
        selectedEvents.clear();
        sName = (TextView) findViewById(R.id.swimmerTitle);
        sName.setText(selectedSwimmers.get(swimmerIndex).toString());
        currentSwimmer = sName.getText().toString();
        if(swimmerIndex != 0)
            sName.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        String[] myResArray = getResources().getStringArray(R.array.events_array2);
        List<String> eventList = Arrays.asList(myResArray);
        lv = (ListView) findViewById(R.id.eventList);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, eventList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view


        selectedEvents.clear();
        String event;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Participants_TABLE " +
                "WHERE Meet_Id='"+MeetActivity.chosenMeetId+"' AND Name='"+currentSwimmer+"'", null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                event = cursor.getString(cursor.getColumnIndex("Event"));
                selectedEvents.add(event); // Add swimmer name to swimmerList
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor.close();

        for(int row=0; row < lv.getCount(); row++)
        {
            //if swimmer is already in meet
            event = lv.getItemAtPosition(row).toString();
            Log.d("Event name", event);

            if (selectedEvents.contains(event))
            {
                Log.d("Checked", "True");
                lv.setItemChecked(row, true); //mark them as checked
            }
            else {
                Log.d("Checked", "False");
            }
        }
    }

    public boolean checkEvents() //check if events selected is > 1 and <= 4
    {
        if(selectedEvents.size() == 0) {
            new MessagePrinter().shortMessage(this, "Please select at least one event");
            return false;
        }
        else if(selectedEvents.size() > 4) {
            new MessagePrinter().shortMessage(this, "Please select no more than 4 events");
            return false;
        }
        else
            return true;
    }

    public void nextSwimmer(View v)
    {
        if(checkEvents())
        {
            //store swimmers selected events
            saveEvents();
            new MessagePrinter().shortMessage(this, MeetInfo.swimmers.get(swimmerIndex).get(0) + " has been saved!");
            if(swimmerIndex < selectedSwimmers.size()-1) //if all swimmers events have not been specified
            {
                swimmerIndex++;
                populateList();
                return;
            }
            for(int swimmer=0; swimmer < MeetInfo.swimmers.size(); swimmer++)
                Log.d("Swimmers events", MeetInfo.swimmers.get(swimmer).toString());

            new RumbleAction(v);
            startActivity(new Intent(this, MeetEditFinishActivity.class));
        }
    }

    public void saveEvents()
    {
        if (selectedEvents.size() == 1) {
            MeetInfo.swimmers.add(swimmerIndex, Arrays.asList(selectedSwimmers.get(swimmerIndex).toString(),
                    selectedEvents.get(0).toString()));
        } else if (selectedEvents.size() == 2) {
            MeetInfo.swimmers.add(swimmerIndex, Arrays.asList(selectedSwimmers.get(swimmerIndex).toString(),
                    selectedEvents.get(0).toString(), selectedEvents.get(1).toString()));
        } else if (selectedEvents.size() == 3) {
            MeetInfo.swimmers.add(swimmerIndex, Arrays.asList(selectedSwimmers.get(swimmerIndex).toString(),
                    selectedEvents.get(0).toString(), selectedEvents.get(1).toString(), selectedEvents.get(2).toString()));
        } else //if number of selected events == 4
        {
            MeetInfo.swimmers.add(swimmerIndex, Arrays.asList(selectedSwimmers.get(swimmerIndex).toString(),
                    selectedEvents.get(0).toString(), selectedEvents.get(1).toString(), selectedEvents.get(2).toString(), selectedEvents.get(3).toString()));
        }
    }
}