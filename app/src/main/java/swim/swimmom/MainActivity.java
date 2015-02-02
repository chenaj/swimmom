package swim.swimmom;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.deleteDatabase("swimmom.db");
        String x = "Database does exist";
        if(doesDatabaseExist(this, "swimmom.db") == false)
            x = "Database does not exist";
        Toast.makeText(this.getApplicationContext(), x, Toast.LENGTH_LONG).show();
    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName)
    {
        //For debugging only
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToProfiles(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
    }
    public void goToMeets(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(MainActivity.this,MeetActivity.class));
    }
    public void goToCutTimes(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(MainActivity.this,CutTimeActivity.class));
    }
    public void goToStatistics(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(MainActivity.this,StatisticActivity.class));
    }
}
