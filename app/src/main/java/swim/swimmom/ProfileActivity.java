package swim.swimmom;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       // ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();

        Cursor  cursor = db.rawQuery("SELECT * FROM "+dop.TABLE_NAME+"",null);

        /*
        if (cursor .moveToFirst())
        {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor
                        .getColumnIndex("Name"));

                list.add(name);
                cursor.moveToNext();
            }
        }*/

         new MessagePrinter(this, ""+cursor.getCount()+" row(s) returned from SELECT query");
        Log.e("Rows returned from SELECT query",""+cursor.getCount()+"");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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


    public void goToProfileAdd(View v) //go to add profile page
    {
        new RumbleAction(v);
        startActivity(new Intent(ProfileActivity.this, ProfileAddActivity.class));
    }
}
