package swim.swimmom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wajihaf56 on 2/10/2015.
 */
public class CutTimeAddActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    EditText cutName;
    Spinner genderSpinner;
    String chosenEvent, errorMsg;
    int chosenPosition;
    Dialog dial;
    ListView lv;
    ArrayList<HashMap<String, String>> cutList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time_add);
        new MyActionBar(getSupportActionBar(), "Add Cut Time"); // Create action bar

        populatePage();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                        // TODO Auto-generated method stub
                        String cutTime = lv.getItemAtPosition(position).toString();
                        chosenEvent = cutList.get(position).get("Event");
                        chosenPosition = position;
                        showNumberDial();
                        Log.d("cut time", cutTime);
                        Log.d("event", chosenEvent);
                        new RumbleAction(view);
                Button saveBtn = (Button) dial.findViewById(R.id.saveBtn);
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPicker minPicker = (NumberPicker) dial.findViewById(R.id.minutesPicker);
                        NumberPicker secPicker = (NumberPicker) dial.findViewById(R.id.secondsPicker);
                        NumberPicker msPicker = (NumberPicker) dial.findViewById(R.id.millisecondsPicker);
                        String time = ""+pad(minPicker.getValue())+":"+pad(secPicker.getValue())+"."+pad(msPicker.getValue())+"";
                        Log.d("Cut Time", time);
                        //update time in map to reflect new time
                        cutList.get(chosenPosition).put("Time", time);
                        updateList(); //update listView
                        dial.dismiss();
                    }
                });
            }
        });
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
        new MenuOptions().MenuOption(curView,item,this,CutTimeActivity.class);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            startActivity(new Intent(this, CutTimeActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    public void populatePage()
    {
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderSpinner.setPrompt("Select...");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.cutGender_array, android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        String[] myResArray = getResources().getStringArray(R.array.events_array2);
        List<String> eventList = Arrays.asList(myResArray);
        for(int i=0; i<eventList.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Event", eventList.get(i));
            map.put("Time", "00:00.00");
            cutList.add(map);
        }
        updateList();
    }

    private void updateList()
    {
        lv = (ListView) findViewById(R.id.eventList);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, cutList, R.layout.cutrows,
                new String[]{"Event", "Time"}, new int[]
                {R.id.event, R.id.eventTime});
        lv.setAdapter(simpleAdapter);
    }

    private void showNumberDial()
    {
        dial = new Dialog(this);
        dial.setTitle(chosenEvent);
        dial.show();
        dial.setContentView(R.layout.numberdial);
        NumberPicker minPicker = (NumberPicker) dial.findViewById(R.id.minutesPicker);
        NumberPicker secPicker = (NumberPicker) dial.findViewById(R.id.secondsPicker);
        NumberPicker msPicker = (NumberPicker) dial.findViewById(R.id.millisecondsPicker);
        EditText minPickerChild = (EditText) minPicker.getChildAt(0);
        EditText secPickerChild = (EditText) secPicker.getChildAt(0);
        EditText msPickerChild = (EditText) msPicker.getChildAt(0);
        //remove ability to focus on each position
        minPickerChild.setFocusable(false);
        secPickerChild.setFocusable(false);
        msPickerChild.setFocusable(false);
        //remove keyboard prompt when clicking time
        minPickerChild.setInputType(InputType.TYPE_NULL);
        secPickerChild.setInputType(InputType.TYPE_NULL);
        msPickerChild.setInputType(InputType.TYPE_NULL);

        minPicker.setMaxValue(59);
        secPicker.setMaxValue(59);
        msPicker.setMaxValue(59);
        minPicker.setMinValue(0);
        secPicker.setMinValue(0);
        msPicker.setMinValue(0);
        //make dial wrap around
        minPicker.setWrapSelectorWheel(true);
        secPicker.setWrapSelectorWheel(true);
        msPicker.setWrapSelectorWheel(true);

        minPicker.setFormatter(new WeightFormatter());
        secPicker.setFormatter(new WeightFormatter());
        msPicker.setFormatter(new WeightFormatter());
    }

    private static String pad(int c) // Add padding to numbers less than ten
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private class WeightFormatter implements NumberPicker.Formatter
    {
        @Override
        public String format(int value) {
            return String.format("%02d", value);
        }
    }

    public void saveCut(View v)
    {
        new RumbleAction(v);
        cutName = (EditText) findViewById(R.id.cutName);
        String name = cutName.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        boolean validInput = true;
        errorMsg = "";

        if(name.isEmpty()) {
            errorMsg = "Please enter a cut name";
            validInput = false;
        }
        if(gender.contains("Select")) {
            addNewLine();
            errorMsg += "Please select a gender";
            validInput = false;
        }

        if(validInput)
        {
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();
            String result = dop.insertCut(db, name, gender, cutList);
            if (result == "Success") {
                new MessagePrinter().longMessage(this, "Cut Times Saved!");
                startActivity(new Intent(this, CutTimeActivity.class));
            } else
                new MessagePrinter().longMessage(this, result);
        }
        else
            new MessagePrinter().longMessage(this, errorMsg);
    }

    public void addNewLine() //adds new line to toast message
    {
        if (errorMsg.length() > 0)
            errorMsg += "\r\n";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
