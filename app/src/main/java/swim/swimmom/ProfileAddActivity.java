package swim.swimmom;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileAddActivity extends ActionBarActivity{
    Spinner genderSpinner;
    Spinner gradeSpinner;
    EditText name, gender, grade, school;
    String S_name, S_gender, S_grade, S_school;
    //Button SAVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_add);
////Gender Spinner
       Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);

////Grade Spinner
        Spinner gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradeSpinner.setAdapter(adapter_1);


        /*
        setContentView(R.layout.activity_profile_add);

        name = (EditText) findViewById(R.id.name);
        //gender = (EditText) findViewById(R.id.genderSpinner);
        school = (EditText) findViewById(R.id.school);

        SAVE = (Button) findViewById(R.id.name)

        S_name = name.toString();
        S_school = school.toString();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_add, menu);
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


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
