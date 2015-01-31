package swim.swimmom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!doesDatabaseExist(MainActivity.this, "swimmomdb"))
        {
            DatabaseOperations dop = new DatabaseOperations(MainActivity.this);
            Log.d("Database operations", "Database created bro!");
        }
        else {

            Log.d("Database operations", "Database already exists bro!");
        }

    }

    private static boolean doesDatabaseExist(Context context, String dbName)
    {
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
        //Provides vibrating feedback when button is pressed
        v.performHapticFeedback(10);

        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
    }
    public void goToMeets(View v)
    {
        //Provides vibrating feedback when button is pressed
        v.performHapticFeedback(10);

        startActivity(new Intent(MainActivity.this,MeetActivity.class));
    }
    public void goToCutTimes(View v)
    {
        //Provides vibrating feedback when button is pressed
        v.performHapticFeedback(10);

        startActivity(new Intent(MainActivity.this,CutTimeActivity.class));
    }
    public void goToStatistics(View v)
    {
        //Provides vibrating feedback when button is pressed
        v.performHapticFeedback(10);

        startActivity(new Intent(MainActivity.this,StatisticActivity.class));
    }
}
