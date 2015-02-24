package swim.swimmom;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wajihaf56 on 2/10/2015.
 */
public class AddCutTimes extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    Spinner genderSpinner;
    ListView list;
    ListAdapter adapter1;
    ArrayList<HashMap<String, String>> momList;
    HashMap<String, String> eventMap;
    ArrayList<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time_add);
        new MyActionBar(getSupportActionBar(), "Add Cut Time"); // Create action bar

        popForm();

        events = new ArrayList<String>(){{
            add("200yd. Medley Relay");
            add("200yd. Freestyle");
            add("200yd. Indv. Medley");
            add("50yd. Freestyle");
            add("100yd. Butterfly");
            add("100yd. Freestyle");
            add("500yd. Freestyle");
            add("200yd. Freestyle Relay");
            add("100yd. Backstroke");
            add("100yd. Breaststroke");
            add("400yd. Freestyle Relay");
        }};
        list = (ListView) findViewById(R.id.eventList);
        momList = new ArrayList<>();

        for(int i=0;i<events.size();i++)
        {
            eventMap = new HashMap<>();
            eventMap.put("Event", events.get(i).toString() );
            momList.add(eventMap);
        }


        adapter1 = new SimpleAdapter(this, momList, R.layout.row, new String[]{"1","2"}, null);
        list.setAdapter(adapter1);
    }

    public void popForm()
    {
        //Gender Spinner
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderSpinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
