package swim.swimmom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class StatisticActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Spinner swimmerSpinner = (Spinner) findViewById(R.id.swimmerSpinner);
        swimmerSpinner.setPrompt("Select...");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.swimmer_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        swimmerSpinner.setAdapter(adapter);


        Spinner eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
        eventSpinner.setPrompt("Select...");
        ArrayAdapter Adapter = ArrayAdapter.createFromResource(this, R.array.event_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eventSpinner.setAdapter(Adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(new Intent(this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
