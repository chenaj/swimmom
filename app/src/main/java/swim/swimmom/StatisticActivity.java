package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;


public class StatisticActivity extends ActionBarActivity {

    ArrayList swimmerList = new ArrayList();
    ListView lv;
    Spinner eventSpinner;
    public static String selected_swimmer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        new MyActionBar(getSupportActionBar(), "Statistics"); // Create action bar
        ///////////////////////////////////

        //////////////
        populateList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                // TODO Auto-generated method stub

                selected_swimmer = lv.getItemAtPosition(position).toString();
                Log.d("You selected", selected_swimmer);

                new RumbleAction(view);
                AlertDialog diaBox = viewStats(getApplicationContext(), view);
                diaBox.show();
            }
        });
    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.profileList)
        {
            menu.setHeaderTitle("Statistics Options"); // Title for pop up menu
            menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
            menu.add(Menu.NONE, 0, 0, "View");

        }
    }

    /*public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "ok") {
            goToSwimmerStatistics(this.findViewById(android.R.id.content).getRootView());
        }

        else {
            return false;
        }
        return true;
    }*/
    private AlertDialog viewStats(final Context context, final View view)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Statistics")
                .setMessage("Would you like view " + selected_swimmer+ "'s statistics?")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
//View statistics
                        startActivity(new Intent(context, SwimmerStatisticActivity.class));
                        new RumbleAction(view);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//do nothing
                        dialog.dismiss();
                        new RumbleAction(view);
                    }
                })
                .create();
    }
     /*  DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        String query = "INSERT INTO Statistics_TABLE (Name, Event, Event_Time,Date, Meet_Id) VALUES ( 'Wajiha','200m FS', '48.56', '01/12/15', '01' )";
    try {
        db.execSQL(query);
    }catch (Exception e)
    {
        Log.e("*Query error!", "INSERT FAILED");

    }

        String query2 = "INSERT INTO Statistics_TABLE (Name, Event, Event_Time,Date, Meet_Id) VALUES ('Wajiha','100m B', '24.32', '01/05/15', '01' )";
        db.execSQL(query2);
        String query3 = "INSERT INTO Statistics_TABLE (Name, Event, Event_Time,Date, Meet_Id) VALUES ('Wajiha','100m MR', '23.65', '02/14/15', '02' )";
        db.execSQL(query3);
        String query4 = "INSERT INTO Statistics_TABLE (Name, Event, Event_Time,Date, Meet_Id) VALUES ('Wajiha','150m BS', '45.65', '02/20/15', '02' )";
        db.execSQL(query4);*/

       /* Spinner swimmerSpinner = (Spinner) findViewById(R.id.swimmerSpinner);
        swimmerSpinner.setPrompt("Select...");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.swimmer_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        swimmerSpinner.setAdapter(adapter);


        Spinner eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
        eventSpinner.setPrompt("Select...");
        ArrayAdapter Adapter = ArrayAdapter.createFromResource(this, R.array.events_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eventSpinner.setAdapter(Adapter);*/
    //}



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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
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
        //sort swimmerList alphabetically
        Collections.sort(swimmerList);
        lv = (ListView) findViewById(R.id.profileList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
    }

    public void goToSwimmerStatistics(View v) //
    {
        new RumbleAction(v);
        startActivity(new Intent(this, SwimmerStatisticActivity.class));
    }


}

