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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class ProfileActivity extends ActionBarActivity{

    public ArrayList swimmerList = new ArrayList(); // Stores list of swimmer names
    public static String chosenSwimmer = ""; // swimmer chosen for doing editing on
    private TextView swCounter;
    ListView lv;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new MyActionBar(getSupportActionBar(), "Profiles"); // Create action bar

        populateList();
        registerForContextMenu(lv); //enable clicking on list items
        // When user clicks a profile in table
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                chosenSwimmer = lv.getItemAtPosition(position).toString();
                Log.d("You selected", chosenSwimmer);
                new RumbleAction(view);
                view.showContextMenu();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                chosenSwimmer = lv.getItemAtPosition(position).toString();
                Log.d("You selected", chosenSwimmer);
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.profileList)
        {
            menu.setHeaderTitle("Profile Options"); // Title for pop up menu
            menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
            menu.add(Menu.NONE, 0, 0, "Edit");
            menu.add(Menu.NONE, 0, 0, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Edit") {
            goToProfileEdit(this.findViewById(android.R.id.content).getRootView());
        }
        else if (item.getTitle() == "Delete") {
            deleteProfiles(this.findViewById(android.R.id.content).getRootView());
        }
        else
            return false;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MainActivity.class );
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
        cursor.close();
        //sort swimmerList alphabetically
        Collections.sort(swimmerList);
        lv = (ListView) findViewById(R.id.profileList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view

        //Prints out the current number of profiles out of 10
        int tempCount = swimmerList.size();
        swCounter = (TextView) findViewById(R.id.counter);
        swCounter.setText(String.valueOf(tempCount) + "/10");
    }

    private AlertDialog AskOption(final Context context, final View view)
    {
        AlertDialog dialogBox = new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete this profile?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//do nothing on cancel
                        dialog.dismiss();
                        new RumbleAction(view);
                        DatabaseOperations dop = new DatabaseOperations(context);
                        SQLiteDatabase db = dop.getWritableDatabase();
                        dop.deleteProfile(db, chosenSwimmer); //delete this profile
                        new MessagePrinter().shortMessage(context, "Profile Deleted!");
                        populateList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//delete selected profile
                        dialog.dismiss();
                        new RumbleAction(view);
                    }
                })
                .create();
        return dialogBox;
    }

    public void goToProfileAdd(View v) //go to add profile page
    {
        new RumbleAction(v);
        startActivity(new Intent(this, ProfileAddActivity.class));
    }

    public void goToProfileEdit(View v) //go to edit profile page
    {
        new RumbleAction(v);
        startActivity(new Intent(this, ProfileEditActivity.class));
    }

    public void deleteProfiles(View v) //when delete is pressed
    {
        new RumbleAction(v);
        AlertDialog diaBox = AskOption(this, v);
        diaBox.show();
    }
}