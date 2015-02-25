package swim.swimmom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MeetCreateSwimmersEventsActivity extends ActionBarActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    HashMap<String, List<String>> swimmerProfiles;
    List <String> eventList;
    ExpandableListView elv;
    SwimmerAdapter sAdapter;
    ArrayList selectedSwimmers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create_swimmers_events);
        new MyActionBar(getSupportActionBar(), "Select Events"); // Create action bar

        //populate array with list of all events
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

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

        //track child checked state here
        //new MessagePrinter().shortMessage(this, "Parent: " + groupPosition + " Child:" + childPosition + "");
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    public void toggle(View v)
    {
        CheckedTextView cView = (CheckedTextView) v.findViewById(R.id.childTextView);
        if (cView.isSelected())
        {
            cView.setSelected(false);
            cView.setCheckMarkDrawable (android.R.drawable.checkbox_off_background);
        }
        else
        {
            cView.setSelected(true);
            cView.setCheckMarkDrawable (android.R.drawable.checkbox_on_background);
        }
    }

    public void populateList()
    {
        selectedSwimmers = MeetCreateSwimmersActivity.selectedSwimmers; // Stores list of swimmer names
        //**Get list of selected swimmers and add events list under each swimmer

        elv = (ExpandableListView) findViewById(R.id.swimmerList);
        swimmerProfiles = DataProvider.getInfo(selectedSwimmers);
        eventList = new ArrayList<>(swimmerProfiles.keySet());
        sAdapter = new SwimmerAdapter(this, swimmerProfiles, eventList);
        elv.setAdapter(sAdapter);
        elv.setOnChildClickListener(this);
        elv.setOnGroupClickListener(this);
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
