package swim.swimmom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends ActionBarActivity {

   DatabaseOperations obj;
    public boolean isThereProfiles = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        obj = new DatabaseOperations(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Checks if there is any profiles on app
        if(obj.numContent() != 0)
            isThereProfiles = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        // Handle item selection
        return super.onOptionsItemSelected(item);
    }


    public void goToProfiles(View v)
    {
            new RumbleAction(v);
            startActivity(new Intent(this, ProfileActivity.class));
    }
    public void goToMeets(View v)
    {
        if(isThereProfiles == true) {
            new RumbleAction(v);
            startActivity(new Intent(this, MeetActivity.class));
        }
        else {
            new MessagePrinter().longMessage(this, "Please create a profile");
        }
    }
    public void goToCutTimes(View v)
    {
        if(isThereProfiles == true) {
            new RumbleAction(v);
            startActivity(new Intent(this, CutTimeActivity.class));
        }
        else {
            new MessagePrinter().longMessage(this, "Please create a profile");
        }
    }
    public void goToStatistics(View v)
    {
        if(isThereProfiles == true) {
            new RumbleAction(v);
            startActivity(new Intent(this, StatisticActivity.class));
        }
        else {
            new MessagePrinter().longMessage(this, "Please create a profile");
        }
    }
}
