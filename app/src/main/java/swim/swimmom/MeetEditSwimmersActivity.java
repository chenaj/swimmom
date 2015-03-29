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
import java.util.Collections;
import java.util.HashSet;


public class MeetEditSwimmersActivity extends ActionBarActivity {

    ArrayList swimmerList = new ArrayList(); // Stores list of swimmer names
    public static ArrayList selectedSwimmers = new ArrayList(); // Swimmers swimming in this meet
    public static ArrayList removedSwimmers = new ArrayList();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_edit_swimmers);
        new MyActionBar(getSupportActionBar(), "Select Swimmers"); // Create action bar


        //find swimmers currently in meet and mark them as checked
        selectedSwimmers.clear();
        removedSwimmers.clear();
        swimmerList.clear();
        String name;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Participants_TABLE WHERE Meet_Id='"+MeetActivity.chosenMeetId+"'", null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                name = name.trim();
                selectedSwimmers.add(name); // Add swimmer name to swimmerList
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor.close();
        //remove name duplicates
        HashSet hs = new HashSet();
        hs.addAll(selectedSwimmers);
        selectedSwimmers.clear();
        selectedSwimmers.addAll(hs);

        Log.d("Selected swimmers", selectedSwimmers.toString());
        MeetInfo.swimmers.clear();
        populateList();

        for(int row=0; row < lv.getCount(); row++)
        {
            //if swimmer is already in meet
            name = lv.getItemAtPosition(row).toString();
            Log.d("Name", name);

            if (selectedSwimmers.contains(name))
            {
                Log.d("Checked", "True");
                lv.setItemChecked(row, true); //mark them as checked
            }
            else {
                Log.d("Checked", "False");
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                String name = lv.getItemAtPosition(position).toString();
                if(!selectedSwimmers.contains(name)) // if name is unchecked prior to click
                {
                    if(removedSwimmers.contains(name))
                        removedSwimmers.remove(name);
                    selectedSwimmers.add(name);      // add name of swimmer
                }
                else {
                    if(selectedSwimmers.contains(name) && !removedSwimmers.contains(name))
                        removedSwimmers.add(name);
                    selectedSwimmers.remove(name); // if name is already checked remove them
                }
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
        new MenuOptions().MenuOption(curView,item,this,MeetEditActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populateList()
    {
        swimmerList.clear();
        String name;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile_TABLE",null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                swimmerList.add(name); // Add swimmer name to swimmerList
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor.close();
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
        Log.d("Selected swimmers", "_____");
        for(int i=0; i<selectedSwimmers.size(); i++) {
            Log.d("Swimmer" + i + "", selectedSwimmers.get(i).toString());
        }

        new RumbleAction(v);
        startActivity(new Intent(this,MeetEditEventsActivity.class));
    }
}
