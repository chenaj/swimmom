package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class MeetSave extends ActionBarActivity {

    ListView lv;
    ArrayList swimmerList = new ArrayList(); // Stores list of swimmer names
    TextView opponent, location, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_save);
        new MyActionBar(getSupportActionBar(), "Verify Meet Info"); // Create action bar

        populatePage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,MeetCreateSwimmersEventsActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populatePage()
    {
        opponent = (TextView) findViewById(R.id.opponent);
        location = (TextView) findViewById(R.id.location);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        opponent.setText(MeetInfo.opponent);
        location.setText(MeetInfo.location);
        date.setText(MeetInfo.date);
        time.setText(MeetInfo.time);

        for(int i=0; i < MeetInfo.swimmers.size(); i++)
            swimmerList.add(MeetInfo.swimmers.get(i).get(0).toString());
        //sort swimmerList alphabetically
        Collections.sort(swimmerList);
        lv = (ListView) findViewById(R.id.profileList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
    }

    public boolean checkMeetInfo()
    {
        if (MeetInfo.date != "") {
            //save meet information to database
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();
            String result = dop.insertMeet(db, MeetInfo.opponent, MeetInfo.location, MeetInfo.date, MeetInfo.time);
            if (result == "Success")
            {
                //if meet successfully saved, link swimmers & their events to this meet
                //insert into participants table using this meets id
                String meet_id = "";
                Cursor cursor = db.rawQuery("SELECT Meet_Id FROM Meet_TABLE WHERE date='"+MeetInfo.date+"'",null);
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        meet_id = cursor.getString(cursor.getColumnIndex("Meet_Id"));
                        cursor.moveToNext(); // Move to next row retrieved
                    }
                }
                cursor.close();
                if(!meet_id.contentEquals(""))
                {
                    //for each swimmer, insert each event they're swimming in
                    for (int row = 0; row < MeetInfo.swimmers.size(); row++)
                    {
                        String swimmer = MeetInfo.swimmers.get(row).get(0);
                        for (int col = 1; col < MeetInfo.swimmers.get(row).size(); col++)
                        {
                            String event = MeetInfo.swimmers.get(row).get(col);
                            dop.insertParticipants(db, meet_id, swimmer, event);
                        }
                    }
                }
                new MessagePrinter().shortMessage(this, "Meet Created!");
                return true;

            } else {
                new MessagePrinter().longMessage(this, result);
                return false;
            }
        } else {
            new MessagePrinter().longMessage(this, "Please complete all meet info first");
            return false;
        }
    }

    public void cancelMeet(View v)
    {
        AlertDialog diaBox = deleteMeetDialog(this, v);
        diaBox.show();
    }

    public void saveMeet(View v)
    {
        if(checkMeetInfo()) //validate meet info before saving
        {
            MeetInfo.clearInfo(); // clear temporary meet info upon successful insertion
            startActivity(new Intent(this, MeetActivity.class));
        }
    }

    private AlertDialog deleteMeetDialog(final Context context, final View view)
    {
        AlertDialog dialogBox = new AlertDialog.Builder(this)
//set message, title, and icon
                .setTitle("Cancel Meet")
                .setMessage("Are you sure you want to cancel creating this meet?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//do nothing on cancel
                        dialog.dismiss();
                        new RumbleAction(view);
                        MeetInfo.clearInfo();
                        startActivity(new Intent(context, MeetActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//delete selected profile
                        dialog.dismiss();
                        new RumbleAction(view);
                        return;
                    }
                })
                .create();
        return dialogBox;
    }
}