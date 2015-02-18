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

    Spinner gradeSpinner;
    EditText schoolField;
    TextView nameField, genderField;
    String S_name, S_gender, S_grade, S_school;
    String errorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        new MyActionBar(getSupportActionBar(), "Edit Swimmer"); // Create action bar

        S_name = ProfileActivity.chosenSwimmer;
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
        TextView nameField = (TextView) findViewById(R.id.name);
        EditText schoolField = (EditText) findViewById(R.id.school);
        TextView genderField = (TextView) findViewById(R.id.gender);
        nameField.setText(S_name);
        schoolField.setText(S_school); //set school name
        genderField.setText(S_gender);

        //--Grade Spinner--//
        Spinner gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        ArrayAdapter adapter_1;
        adapter_1 = ArrayAdapter.createFromResource(this, R.array.grade_array2, android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter_1); // Apply adapter to the spinner
        ArrayAdapter myAdapter = (ArrayAdapter) gradeSpinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdapter.getPosition(S_grade); //find position of current grade
        gradeSpinner.setSelection(spinnerPosition); //set grade in spinner
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View curView = this.findViewById(android.R.id.content).getRootView();
        new RumbleAction(curView);
        // Handle item selection
        new MenuOptions().MenuOption(curView,item,this,ProfileEditActivity.class,ProfileActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void goToProfiles(View v) //navigate back to profile page when update is pressed
    {
        //Retrieve values from input fields
        nameField = (TextView) findViewById(R.id.name);
        schoolField = (EditText) findViewById(R.id.school);
        genderField = (TextView) findViewById(R.id.gender);
        gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);

        //Convert them to strings
        S_name = nameField.getText().toString();
        S_school = schoolField.getText().toString();
        S_gender= genderField.getText().toString();
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
                new MessagePrinter().shortMessage(this, "Changes Saved!");
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