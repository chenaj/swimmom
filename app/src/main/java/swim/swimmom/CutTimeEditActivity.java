package swim.swimmom;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class CutTimeEditActivity extends ActionBarActivity {
    EditText cutName, gender;
    String chosenEvent, errorMsg;
    int chosenPosition;
    Dialog dial;
    ListView lv;
    ArrayList<HashMap<String, String>> cutList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time_edit);
        new MyActionBar(getSupportActionBar(), "View Cut Time"); // Create action bar

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
        new MenuOptions().MenuOption(curView,item,this,MainActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void populatePage()
    {
        cutName = (EditText) findViewById(R.id.cutName);
        gender = (EditText) findViewById(R.id.gender);
        cutName.setText(""+CutInfo.cutName);
        gender.setText(""+CutInfo.gender);

        cutList = CutInfo.cutInfo; //get all cut info from selected cut
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

    public void updateCut(View v)
    {
        new RumbleAction(v);
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        String result = dop.updateCut(db, CutInfo.cutName, CutInfo.gender, cutList);
        if (result == "Success") {
            new MessagePrinter().shortMessage(this, "Changes Saved!");
            startActivity(new Intent(this, CutTimeActivity.class));
        } else
            new MessagePrinter().longMessage(this, result);
    }
}
