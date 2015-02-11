package swim.swimmom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class ProfileEditActivity extends ActionBarActivity{

    Spinner genderSpinner, gradeSpinner; //for gender and grade spinner drop-downs
    EditText nameField, schoolField; //for name and school text fields
    String S_name, S_gender, S_grade, S_school;
    String errorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        S_name = ProfileActivity.nameToEdit;
        errorMsg = "";
        DatabaseOperations dop = new DatabaseOperations(this);
        SQLiteDatabase db = dop.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile_TABLE WHERE Name='"+S_name+"'",null);
        if (cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                //get rest of selected user info
                S_school = cursor.getString(cursor.getColumnIndex("School"));
                S_gender = cursor.getString(cursor.getColumnIndex("Gender"));
                S_grade = cursor.getString(cursor.getColumnIndex("Grade"));
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        // Populate fields on page with existing swimmer info
        EditText nameField = (EditText) findViewById(R.id.name);
        EditText schoolField = (EditText) findViewById(R.id.school);
        nameField.setText(S_name);
        nameField.setFocusable(false); //name field cannot be edited
        nameField.setClickable(false);
        schoolField.setText(S_school);

        //Gender Spinner
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter); // Apply adapter to the spinner

        //Grade Spinner
        Spinner gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter_1); // Apply adapter to the spinner
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

    public void goToProfiles(View v) //navigate back to profile page when update is pressed
    {
        //Retrieve values from input fields
        nameField = (EditText) findViewById(R.id.name);
        schoolField = (EditText) findViewById(R.id.school);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);

        //Convert them to strings
        S_name = nameField.getText().toString();
        S_school = schoolField.getText().toString();
        S_gender= genderSpinner.getSelectedItem().toString();
        S_grade = gradeSpinner.getSelectedItem().toString();

        new RumbleAction(v);
        if( validInput() ) //if all user input fields are valid
        {
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();

            //Update swimmer information in database
            String result = dop.updateProfile(db, S_name, S_school, S_gender, S_grade);
            if(result == "Success") //if update is successful
            {
                new MessagePrinter().shortMessage(this, "Swimmer Updated!");
                startActivity(new Intent(this, ProfileActivity.class));
            }
            else //if update fails i.e., display returned error message
            {
                errorMsg = result;
                new MessagePrinter().longMessage(this, errorMsg);
                errorMsg = "";
                return;
            }
        }
        else {
            new MessagePrinter().longMessage(this, errorMsg);
            errorMsg = "";
            return;
        }
    }

    boolean validInput() //check if input fields are all filled in correctly
    {
        boolean validInput = true;

        if (S_name.length() < 1) //if name field is empty
        {
            //Toast.makeText(this.getApplicationContext(), "You did not enter a name", Toast.LENGTH_SHORT).show();
            addNewLine();
            errorMsg += "You did not enter a name";
            validInput = false;
        }
        if (S_gender.matches("Select...")) //if gender is not selected
        {
            addNewLine();
            errorMsg += "You did not select a gender";
            validInput = false;
        }
        if (S_grade.matches("Select...")) //if grade is not selected
        {
            addNewLine();
            errorMsg += "You did not select a grade";
            validInput = false;
        }
        if (S_school.length() < 1) //if school field is empty
        {
            addNewLine();
            errorMsg += "You did not enter a school";
            validInput = false;
        }
        return validInput;
    }

    public void addNewLine() //adds new line to display multiple error messages in single toast message
    {
        if(errorMsg.length() > 0)
            errorMsg += "\r\n";
    }
}