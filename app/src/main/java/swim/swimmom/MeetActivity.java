package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MeetActivity extends ActionBarActivity {

    public static String chosenMeet = ""; // Meet chosen for doing editing on
    public static String chosenMeetId = "";
    public static String chosenMeetDate="";
    public static String chosenMeetOpponent="";
    ArrayList<HashMap<String, String>> feedList= new ArrayList<>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        new MyActionBar(getSupportActionBar(), "Meets"); // Create action bar

        populateList();
        registerForContextMenu(lv); //enable long clicking on list items
        // When user short clicks a meet in table
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub

                chosenMeetId = feedList.get(position).get("Meet_Id");
                chosenMeetDate = feedList.get(position).get("Date");
                chosenMeetOpponent = feedList.get(position).get("Opponent");
                Log.d("Meet_Id", chosenMeetId);
                Log.d("Meet Date", chosenMeetDate);
                Log.d("Meet Opponent", chosenMeetOpponent);
                new RumbleAction(view);
                AlertDialog diaBox = beginMeetDialog(getApplicationContext(), view);
                diaBox.show();
            }
        });
        // When user long clicks a meet in table
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                chosenMeet = lv.getItemAtPosition(position).toString();
                chosenMeetId = feedList.get(position).get("Meet_Id");
                chosenMeetDate = feedList.get(position).get("Date");
                chosenMeetOpponent = feedList.get(position).get("Opponent");
                Log.d("Meet_Id", chosenMeetId);
                Log.d("Meet Date", chosenMeetDate);
                Log.d("Meet Opponent", chosenMeetOpponent);
                new RumbleAction(view);
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.meetList)
        {
            menu.setHeaderTitle("Meet Options"); // Title for pop up menu
            menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
            menu.add(Menu.NONE, 0, 0, "Edit");
            menu.add(Menu.NONE, 0, 0, "Delete");
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Edit") {
            goToEditMeet(this.findViewById(android.R.id.content).getRootView());
        }
        else if (item.getTitle() == "Delete") {
            deleteMeet(this.findViewById(android.R.id.content).getRootView());
        }
        else
            return false;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);

        if (item.getItemId() == R.id.info)
        {
            AlertDialog diaBox = meetInfoDialog(getApplicationContext(), curView);
            diaBox.show();
        }

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
        String Meet_Id, opponent, date;
        feedList.clear();
        lv = (ListView) findViewById(R.id.meetList);
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Meet_TABLE ORDER BY Date",null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Meet_Id = cursor.getString(cursor.getColumnIndex("Meet_Id"));
                opponent = cursor.getString(cursor.getColumnIndex("Opponent"));
                date = cursor.getString(cursor.getColumnIndex("Date"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Date", date);
                map.put("vs.", "vs.");
                map.put("Opponent", opponent);
                map.put("Meet_Id", Meet_Id);
                feedList.add(map);
                cursor.moveToNext(); // Move to next row retrieved
            }
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, feedList, R.layout.listcolumns,
                new String[]{"Date", "vs.", "Opponent", "Meet_Id"}, new int[]
                {R.id.textViewDate, R.id.textViewVs, R.id.textViewOpponent, R.id.textViewMeetId});
        lv.setAdapter(simpleAdapter);
        /*
        //Sort meetList by date here
        lv = (ListView) findViewById(R.id.meetList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, meetList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
        */
    }

    private AlertDialog meetInfoDialog(final Context context, final View view)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Meet Info")
                .setMessage("- Short click on a meet to begin meet \n\n" +
                            "- Long click on a meet to edit or delete it")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private AlertDialog beginMeetDialog(final Context context, final View view)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Start Meet")
                .setMessage("Would you like to begin this meet?")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//do nothing on cancel
                        startActivity(new Intent(context, MeetSwimmersEvents.class));
                        new RumbleAction(view);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//begin meet
                        dialog.dismiss();
                        new RumbleAction(view);
                    }
                })
                .create();
    }

    private AlertDialog deleteMeetDialog(final Context context, final View view)
    {
        return new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Delete Meet")
                .setMessage("Are you sure you want to delete this meet?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//do nothing on cancel
                        dialog.dismiss();
                        new RumbleAction(view);
                        DatabaseOperations dop = new DatabaseOperations(context);
                        SQLiteDatabase db = dop.getWritableDatabase();
                        dop.deleteMeet(db, chosenMeetId); //delete this profile
                        new MessagePrinter().shortMessage(context, "Meet Deleted!");
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
    }

    public void goToCreateMeet(View v)
    {
        new RumbleAction(v);
        MeetInfo.clearInfo();
        startActivity(new Intent(this, MeetCreateActivity.class));
    }

    public void goToEditMeet(View v)
    {
        new RumbleAction(v);
        MeetInfo.clearInfo();
        startActivity(new Intent(this, MeetEditActivity.class));
    }

    public void deleteMeet(View v) //when delete is pressed
    {
        new RumbleAction(v);
        AlertDialog diaBox = deleteMeetDialog(this, v);
        diaBox.show();
    }
}
