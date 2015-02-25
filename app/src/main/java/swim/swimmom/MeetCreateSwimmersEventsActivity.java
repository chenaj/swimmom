package swim.swimmom;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MeetCreateSwimmersEventsActivity extends ActionBarActivity {

    String[] eventArray; // Stores list of swimmer names
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create_swimmers_events);
        new MyActionBar(getSupportActionBar(), "Select Events"); // Create action bar

        //populate array with list of all events
        eventArray = getResources().getStringArray(R.array.events_array);
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_create_swimmers_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MeetCreateSwimmersActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populateList()
    {
        ArrayList selectedSwimmers = MeetCreateSwimmersActivity.selectedSwimmers; // Stores list of swimmer names
        //**Get list of selected swimmers and add events list under each swimmer

        lv = (ListView) findViewById(R.id.swimmerList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectedSwimmers);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
    }

    public void goToMeets(View v)
    {
        new RumbleAction(v);
        if(MeetInfo.date != "")
        {
            //save meet information to database
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();
            String result = dop.insertMeet(db, MeetInfo.opponent, MeetInfo.location, MeetInfo.date, MeetInfo.time);
            if (result == "Success") {
                new MessagePrinter().longMessage(this, "Meet Created!");
                //clear previous meet info
                MeetInfo.clearInfo(); // clear temporary meet info upon successful insertion
                startActivity(new Intent(this, MeetActivity.class));
            } else {
                new MessagePrinter().longMessage(this, result);
            }
        }
        else
            new MessagePrinter().longMessage(this, "Please fill in meet info first");
    }
}
