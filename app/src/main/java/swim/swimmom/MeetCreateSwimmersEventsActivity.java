package swim.swimmom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;


public class MeetCreateSwimmersEventsActivity extends ActionBarActivity {

    String[] eventArray; // Stores list of swimmer names
    public
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
        new MenuOptions().MenuOption(curView,item,this,MeetCreateSwimmersEventsActivity.class,MeetCreateSwimmersActivity.class);
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
        new MessagePrinter().longMessage(this, "Meet Created!");
        startActivity(new Intent(this, MeetActivity.class));
    }
}
