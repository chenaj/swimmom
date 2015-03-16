package swim.swimmom;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class ProfileAddActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    EditText nameField, schoolField; //for name and school text fields
    Spinner genderSpinner, gradeSpinner; //for gender and grade spinner drop-downs
    String S_name, S_school, S_gender, S_grade;
    String errorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_add);
        new MyActionBar(getSupportActionBar(), "Add a Swimmer"); // Create action bar

        S_name = "";
        S_school = "";
        S_gender = "";
        S_grade = "";
        resetForm(getWindow().getDecorView().findViewById(android.R.id.content));
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
        new MenuOptions().MenuOption(curView,item,this, ProfileActivity.class);
        return super.onOptionsItemSelected(item);
    }

    public void resetForm(View v)
    {
        nameField = (EditText) findViewById(R.id.name);
        schoolField = (EditText) findViewById(R.id.school);
        nameField.setText("");
        schoolField.setText("");

        //Gender Spinner
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderSpinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        //Grade Spinner
        Spinner gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);
        gradeSpinner.setPrompt("Select...");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradeSpinner.setAdapter(adapter_1);
        gradeSpinner.setOnItemSelectedListener(this);
    }

    public void goToProfiles(View v) //navigate back to profile page when save is pressed
    {
        //Retrieve values from input fields
        nameField = (EditText) findViewById(R.id.name);
        schoolField = (EditText) findViewById(R.id.school);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        gradeSpinner = (Spinner) findViewById(R.id.gradeSpinner);

        //Convert them to strings
        S_name = nameField.getText().toString();
        S_school = schoolField.getText().toString();
        S_gender = genderSpinner.getSelectedItem().toString();
        S_grade = gradeSpinner.getSelectedItem().toString();

        new RumbleAction(v);
        if (validInput()) //if all user input fields are valid
        {
            DatabaseOperations dop = new DatabaseOperations(this);
            SQLiteDatabase db = dop.getWritableDatabase();
            //Insert swimmer information into database
            S_name = S_name.toLowerCase();
            S_name = toTitleCase(S_name); //format name (i.e., john johnson -> John Johnson)
            String result = dop.insertProfile(db, S_name, S_gender, S_grade, S_school);
            if (result.equals("Success")) //if insert is successful
            {
                new MessagePrinter().shortMessage(this, "Swimmer Saved!");
                startActivity(new Intent(this, ProfileActivity.class));
            } else //if insert fails i.e., display returned error message
            {
                errorMsg = result;
                new MessagePrinter().longMessage(this, errorMsg);
                errorMsg = "";
            }
        }
        else
        {
            new MessagePrinter().longMessage(this, errorMsg);
            errorMsg = "";
        }
    }

    public static String toTitleCase(String givenString) // uppercase first letter of each word in string
    {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0)))
                    .append(anArr.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    boolean validInput() //check if input fields are all filled in correctly
    {
        boolean validInput = true;

        Log.d("Name", S_name);
        Log.d("School", S_school);
        Log.d("Gender", S_gender);
        Log.d("Grade", S_grade);


        if (S_name.length() < 1) //if name field is empty
        {
            addNewLine();
            errorMsg = "Please enter a name";
            validInput = false;
        }
        else if (containsDigit(S_name)) //if name contains digits
        {
            errorMsg = "Name cannot contain digits";
            validInput = false;
        }

        if (S_school.length() < 1) //if school field is empty
        {
            addNewLine();
            errorMsg += "Please enter a school";
            validInput = false;
        }
        if (S_gender.matches("Select...")) //if gender is not selected
        {
            addNewLine();
            errorMsg += "Please select a gender";
            validInput = false;
        }
        if (S_grade.matches("Select...")) //if grade is not selected
        {
            addNewLine();
            errorMsg += "Please select a grade";
            validInput = false;
        }
        return validInput;
    }

    public final boolean containsDigit(String s)
    {
        boolean containsDigit = false;
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c))
                    break;
            }
        }
        return containsDigit;
    }

    public void addNewLine() //adds new line to toast message
    {
        if (errorMsg.length() > 0)
            errorMsg += "\r\n";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            TextView myText = (TextView) view;
            //Toast.makeText(this.getApplicationContext(), "You Selected " + " " + myText.getText(), Toast.LENGTH_SHORT).show();
            Log.d("You selected", myText.getText().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}