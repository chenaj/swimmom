package swim.swimmom;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // ActionBar actionBar = getActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(true);

        List<String[]> tableData = new ArrayList<String[]>(); // Stores profile table data
        List<String> swimmerList = new ArrayList<String>(); // Stores list of swimmer names
        String id, name, school, gender, grade;

        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+dop.TABLE_NAME+"",null);

        if (cursor .moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                id = cursor.getString(cursor.getColumnIndex("Id"));
                name = cursor.getString(cursor.getColumnIndex("Name"));
                school = cursor.getString(cursor.getColumnIndex("School"));
                gender = cursor.getString(cursor.getColumnIndex("Gender"));
                grade = cursor.getString(cursor.getColumnIndex("Grade"));

                // Add retrieved row to tableData
                tableData.add(new String[] { id, name, school, gender, grade});
                swimmerList.add(name); // Add swimmer name to swimmerList
                cursor.moveToNext(); // Move to next row retrieved
            }
        }

        ListView lv = (ListView) findViewById(R.id.profileList);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, swimmerList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view

        // new MessagePrinter().shortMessage(this, ""+cursor.getCount()+" Swimmer(s) in profile table");
        // Log.d("Swimmer(s) in profile table",""+cursor.getCount()+"");
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

    public void goToProfileDelete(View v) //go to delete profile page
    {
        new RumbleAction(v);
        startActivity(new Intent(ProfileActivity.this, ProfileDeleteActivity.class));
    }
}

