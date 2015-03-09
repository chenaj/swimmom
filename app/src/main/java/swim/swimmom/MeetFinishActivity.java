package swim.swimmom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MeetFinishActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_finish);
        new MyActionBar(getSupportActionBar(), "Meet Complete"); // Create action bar
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
//do nothing on cancel
                        startActivity(new Intent(context, MeetActivity.class));
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
        return dialogBox;
    }
}
