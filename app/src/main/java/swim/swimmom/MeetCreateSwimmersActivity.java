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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MeetCreateSwimmersActivity extends ActionBarActivity {

    ArrayList swimmerList = new ArrayList(); // Stores list of swimmer names
    public static ArrayList selectedSwimmers = new ArrayList(); // Swimmers swimming in this meet
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create_swimmers);
        new MyActionBar(getSupportActionBar(), "Select Swimmers"); // Create action bar

        populateList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                String name = lv.getItemAtPosition(position).toString();
                if(!selectedSwimmers.contains(name)) // if name is unchecked prior to click
                    selectedSwimmers.add(name);      // add name of swimmer
                else
                    selectedSwimmers.remove(name); // if name is already checked remove them
                new RumbleAction(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_create_swimmers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MeetCreateActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populateList()
    {
        selectedSwimmers.clear();
        swimmerList.clear();
        String name;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile_TABLE",null);
        if (cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                swimmerList.add(name); // Add swimmer name to swimmerList
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        //sort swimmerList alphabetically
        Collections.sort(swimmerList);
        lv = (ListView) findViewById(R.id.profileList);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view

    }

    public void goToSelectEvents(View v)
    {
        //Get list of checked names
        if(selectedSwimmers.size() == 0) //if no swimmers selected
        {
            new MessagePrinter().shortMessage(this, "Please select at least one swimmer");
            return;
        }
        Log.d("Selected swimmers","_____");
        for(int i=0; i<selectedSwimmers.size(); i++) {
            //MeetInfo.swimmers.add(i, Arrays.asList(selectedSwimmers.get(i).toString())); //save swimmer names
            Log.d("Swimmer" + i + "", selectedSwimmers.get(i).toString());
        }

        /*//For debugging
        for(int i=0; i<MeetInfo.swimmers.size(); i++)
            Log.d("Swimmer"+i+"", MeetInfo.swimmers.get(i).toString());*/

        //MeetInfo.swimmers.add(index, Arrays.asList("Swimmer1", "100 FS", "200 BS"));
        //MeetInfo.swimmers.add(index, Arrays.asList("Swimmer2", "200 FS", "200 BS", "50 FS"));

        new RumbleAction(v);
        startActivity(new Intent(this,MeetCreateSwimmersEventsActivity.class));
    }
}
