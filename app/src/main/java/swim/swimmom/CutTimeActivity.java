package swim.swimmom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class CutTimeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cut_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                new RumbleAction(curView);
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.refreshOption:
                new RumbleAction(curView);
                startActivity(new Intent(this, CutTimeActivity.class));
                return true;

            case R.id.mainOption:
                new RumbleAction(curView);
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.profilesOption:
                new RumbleAction(curView);
                startActivity(new Intent(this, ProfileActivity.class));
                return true;

            case R.id.meetsOption:
                new RumbleAction(curView);
                startActivity(new Intent(this, MeetActivity.class));
                return true;

            case R.id.statisticsOption:
                new RumbleAction(curView);
                startActivity(new Intent(this, StatisticActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToAddCutTimes(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(CutTimeActivity.this, AddCutTimes.class));
    }
}
