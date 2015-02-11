package swim.swimmom;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class ProfileActivity extends ActionBarActivity{

    public static String nameToEdit = "";
    ArrayList swimmerList = new ArrayList(); // Stores list of swimmer names
    ArrayList checkedList = new ArrayList(); // List of checked swimmers

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        populateList();
        final ListView lv = (ListView) findViewById(R.id.profileList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                String chosenSwimmer = lv.getItemAtPosition(myItemInt).toString();
                if(!checkedList.contains(chosenSwimmer)) //if swimmer name is checked
                    checkedList.add(chosenSwimmer);
                else //if swimmer name is unchecked, remove them from list
                    checkedList.remove(chosenSwimmer);
                Log.d("Selected item", chosenSwimmer);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private AlertDialog AskOption(final Context context, final View view)
    {
        AlertDialog dialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete the selected profiles?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do nothing
                        dialog.dismiss();
                        new RumbleAction(view);
                        ListView listView = (ListView) findViewById(R.id.profileList);
                        listView.clearChoices();
                        listView.requestLayout();
                        checkedList.clear(); //clear list
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //delete selected profiles
                        dialog.dismiss();
                        new RumbleAction(view);
                        DatabaseOperations dop = new DatabaseOperations(context);
                        SQLiteDatabase db = dop.getWritableDatabase();
                        //For each checked swimmer to delete
                        for (int index = 0; index < checkedList.size(); index++) {
                            String profile = checkedList.get(index).toString(); //get profile
                            dop.deleteProfile(db, profile); //delete this profile
                        }
                        checkedList.clear(); //clear list
                        populateList();
                    }
                })
                .create();
        return dialogBox;
    }

    public void deleteProfiles(View v) //when delete is pressed
    {
        new RumbleAction(v);
        if(checkedList.size() == 0) //if delete clicked with no swimmers selected
        {
            new MessagePrinter().shortMessage(this, "Please select swimmers first");
            return;
        }
        else
        {
            AlertDialog diaBox = AskOption(this, v);
            diaBox.show();
        }
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
        final ListView lv = (ListView) findViewById(R.id.profileList);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
    }

    public void goToProfileAdd(View v) //go to add profile page
    {
        new RumbleAction(v);
        startActivity(new Intent(this, ProfileAddActivity.class));
    }

    public void goToProfileEdit(View v) //go to edit profile page
    {
        new RumbleAction(v);
        if(checkedList.size() == 0) //if no swimmers selected
        {
            new MessagePrinter().shortMessage(this, "Please select a swimmer to edit");
            return;
        }
        else if(checkedList.size() > 1)
        {
            new MessagePrinter().shortMessage(this, "Please select a single swimmer");
            return;
        }
        else
        {
            nameToEdit = checkedList.get(0).toString(); //swimmer name to be reference in edit page
            startActivity(new Intent(this, ProfileEditActivity.class));
        }
    }
}

