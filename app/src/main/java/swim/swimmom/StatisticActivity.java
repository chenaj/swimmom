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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;


public class StatisticActivity extends ActionBarActivity {

    ArrayList swimmerList = new ArrayList();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        new MyActionBar(getSupportActionBar(), "Statistics"); // Create action bar
        populateList();

        /*DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        String query = "INSERT INTO Statistics_TABLE (Name, Event, Event_Time, Meet_Id) VALUES ('')";
        db.execSQL(query);

        Spinner swimmerSpinner = (Spinner) findViewById(R.id.swimmerSpinner);
        swimmerSpinner.setPrompt("Select...");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.swimmer_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        swimmerSpinner.setAdapter(adapter);


        Spinner eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
        eventSpinner.setPrompt("Select...");
        ArrayAdapter Adapter = ArrayAdapter.createFromResource(this, R.array.events_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eventSpinner.setAdapter(Adapter);*/
    }


    public void viewSwimmerStats ()
    {



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MainActivity.class);
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
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
    }

}
