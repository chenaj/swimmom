package swim.swimmom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileAddActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private boolean selectionControl = true;
    Spinner genderSpinner, gradeSpinner; //for gender and grade spinner drop-downs
    EditText name, school; //for name and school text fields
    String S_name, S_gender, S_grade, S_school;
    //Button SAVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_add);

////Gender Spinner
       Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderSpinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

////Grade Spinner
        Spinner gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        gradeSpinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradeSpinner.setAdapter(adapter_1);
        gradeSpinner.setOnItemSelectedListener(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
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

    public void goToProfiles(View v) //navigate back to profile page when save is pressed
    {
        //Retrieve values from input fields
        name = (EditText) findViewById(R.id.name);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        school = (EditText) findViewById(R.id.school);

        //Convert them to strings
        S_name = name.getText().toString();
        S_gender= genderSpinner.getSelectedItem().toString();
        S_grade = gradeSpinner.getSelectedItem().toString();
        S_school = school.getText().toString();

        //Output user input to log
        Log.d("****** Name", S_name);
        Log.d("****** Gender", S_gender);
        Log.d("****** Grade", S_grade);
        Log.d("****** School", S_school);

        new RumbleAction(v);

        if( validInput() )
        {
            new MessagePrinter(this, "Swimmer Saved!");
            startActivity(new Intent(ProfileAddActivity.this, ProfileActivity.class));
        }
        else
            return;
    }

    boolean validInput() //check if input fields are all filled in correctly
    {
        boolean validInput = true;
        if (name.toString().matches("")) //if name field is empty
        {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        if (!S_gender.matches("Male") || !S_gender.matches("Female")) //if name field is empty
        {
            Toast.makeText(this, "You did not select a gender", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        if (S_grade.matches("")) //if name field is empty
        {
            Toast.makeText(this, "You did not select a gender", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        if (name.toString().matches("")) //if name field is empty
        {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
            validInput = false;
        }

        return validInput;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0)
        {
            TextView myText = (TextView) view;
            Toast.makeText(this.getApplicationContext(), "You Selected " + " " + myText.getText(), Toast.LENGTH_SHORT).show();
            Log.d("*****You selected", myText.getText().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}