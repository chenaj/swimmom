package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class MeetFinishActivity extends ActionBarActivity {
    public static ArrayList<HashMap<String,String>> finalStats = MeetSwimmersEvents.finalStats;
    String meetDate = MeetActivity.chosenMeetDate;
    String meetID = MeetActivity.chosenMeetId;
    String meetOpponent = MeetActivity.chosenMeetOpponent;
    ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_finish);
        new MyActionBar(getSupportActionBar(), "Meet Complete"); // Create action bar

        Log.d("Meet Date", meetDate);
        Log.d("Meet ID", meetID);
        Log.d("Meet Opponent", meetOpponent);

        lv = (ListView) findViewById(R.id.swimmerList);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, finalStats, R.layout.finalstatsrow,
                new String[]{"Name", "Event", "Time"}, new int[]{R.id.name, R.id.event, R.id.eventTime});
        lv.setAdapter(simpleAdapter); // Apply the adapter to the list view
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MeetActivity.class);
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog saveMeetDialog(final Context context, final View view)
    {
        AlertDialog dialogBox = new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Save Meet")
                .setMessage("Would you like to save this meet?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//save meet
                        saveStats();
                        startActivity(new Intent(context, MeetActivity.class));
                        new RumbleAction(view);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//cancel
                        dialog.dismiss();
                        new RumbleAction(view);
                    }
                })
                .create();
        return dialogBox;
    }

    public void saveStats()
    {
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();

        for (int i=0; i<finalStats.size();i++)
        {
            Log.d("Name", finalStats.get(i).get("Name"));
            Log.d("Event", finalStats.get(i).get("Event"));
            Log.d("Time", finalStats.get(i).get("Time"));

            dop.insertStatistics(db, finalStats.get(i).get("Name"), finalStats.get(i).get("Event"),
                    finalStats.get(i).get("Time"), meetDate, meetID, meetOpponent);
        }
    }

    public void saveMeet(View v)
    {
        AlertDialog ad= saveMeetDialog(this,v);
        ad.show();
    }
}
