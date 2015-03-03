package swim.swimmom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class CutTimeActivity extends ActionBarActivity {

    ArrayList<HashMap<String, String>> cutList= new ArrayList<>();
    public static String chosenCut = ""; // Cut time chosen for doing editing on
    public static String chosenGender = "";
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time);
        new MyActionBar(getSupportActionBar(), "Cut Times"); // Create action bar

        populateList();
        CutInfo.clearInfo();
        registerForContextMenu(lv); //enable long clicking on list items
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                // TODO Auto-generated method stub
                chosenCut = cutList.get(position).get("Name");
                chosenGender = cutList.get(position).get("Gender");
                Log.d("Cut Name", chosenCut);
                Log.d("Chosen Gender", chosenGender);
                new RumbleAction(view);
                view.showContextMenu();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.cutsList)
        {
            menu.setHeaderTitle("Cut Time Options"); // Title for pop up menu
            menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
            menu.add(Menu.NONE, 0, 0, "View/Edit");
            menu.add(Menu.NONE, 0, 0, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "View/Edit") {
            goToEditCutTimes(this.findViewById(android.R.id.content).getRootView());
        }
        else if (item.getTitle() == "Delete") {
            deleteCutTimes(this.findViewById(android.R.id.content).getRootView());
        }
        else
            return false;
        return true;
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
        new MenuOptions().MenuOption(curView,item,this,MainActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populateList()
    {
        cutList.clear();
        String name;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BoysCuts_TABLE",null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Name", name);
                map.put("Gender", "Boys");
                cutList.add(map);
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor = db.rawQuery("SELECT * FROM GirlsCuts_TABLE",null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Name", name);
                map.put("Gender", "Girls");
                cutList.add(map);
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        cursor.close();
        //Collections.sort(cutList);

        lv = (ListView) findViewById(R.id.cutsList);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, cutList, R.layout.cutrows,
                new String[]{"Name", "Gender"}, new int[]
                {R.id.event, R.id.eventTime});
        lv.setAdapter(simpleAdapter);

        /*
        lv = (ListView) findViewById(R.id.cutsList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //multiple choice list i.e., checked or unchecked
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cutList);
        lv.setAdapter(listAdapter); // Apply the adapter to the list view
        */
    }

    public void goToAddCutTimes(View v)
    {
        new RumbleAction(v);
        startActivity(new Intent(this, CutTimeAddActivity.class));
    }

    public void goToEditCutTimes(View v)
    {
        new RumbleAction(v);
        //find information on selected cut time
        CutInfo.clearInfo();
        CutInfo.cutName = chosenCut;
        CutInfo.gender = chosenGender;
        String event, time;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+chosenCut+"",null);

        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                event = cursor.getString(cursor.getColumnIndex("Event"));
                time = cursor.getString(cursor.getColumnIndex("Time"));
                HashMap<String, String> map = new HashMap<>();
                map.put("Event", event);
                map.put("Time", time);
                CutInfo.cutInfo.add(map);
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        startActivity(new Intent(this, CutTimeEditActivity.class));
    }

    public void deleteCutTimes(View v)
    {
        new RumbleAction(v);
        CutInfo.clearInfo();
        CutInfo.cutName = chosenCut;
        CutInfo.gender = chosenGender;
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        String result = dop.deleteCut(db, CutInfo.cutName, CutInfo.gender);
        if (result == "Success") {
            new MessagePrinter().shortMessage(this, "Cut Deleted!");
            startActivity(new Intent(this, CutTimeActivity.class));
        } else
            new MessagePrinter().shortMessage(this, result);


    }
}
