package swim.swimmom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        startActivity(new Intent(this,ProfileActivity.class));
    }
    public void goToMeets(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(this,MeetActivity.class));
    }
    public void goToCutTimes(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(this,CutTimeActivity.class));
    }
    public void goToStatistics(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(this,StatisticActivity.class));
    }
}
