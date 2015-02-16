package swim.swimmom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by wajihaf56 on 1/30/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper{

    //** variable represents name in code, the string represents column name in table
    public static final String DATABASE_NAME = "swimmom.db";
    public static final int DATABASE_VERSION = 3;

    public static final String CREATE_Profile_TABLE =
            "CREATE TABLE Profile_TABLE" +
            "(" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Name varchar(60)," +
            "School varchar(100)," +
            "Gender varchar(6)," +
            "Grade varchar(9)" +
            ");";

    public static final String CREATE_Meet_TABLE =
            "CREATE TABLE Meet_TABLE" +
                    "(" +
                    "Meet_Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Date date NOT NULL," +
                    "Location varchar(100)," +
                    "Time varchar(5)," +
                    "Opponent varchar(100)" +
                    ");";

    public static final String CREATE_Participants_TABLE =
            "CREATE TABLE Participants_TABLE" +
                    "(" +
                    "Meet_Id INTEGER," +
                    "Name varchar(60)," +
                    "Event varchar(50)," +
                    "PRIMARY KEY (Meet_Id),"+
                    "FOREIGN KEY (Meet_Id) REFERENCES Meet_TABLE(Meet_Id)"+
                    ");";
                    
    public static final String CREATE_Statistics_TABLE =
            "CREATE TABLE Statistics_TABLE" +
                    "(" +
                    "Name varchar(60)," +
                    "Event varchar(50)," +
                    "Event_Time varchar(6)," +
                    "Meet_Id INTEGER," +
                    "PRIMARY KEY (Name),"+
                    "FOREIGN KEY (Meet_Id) REFERENCES Meet_TABLE(Meet_Id)"+
                    ");";                
                    
                    
    //Example Event retrieve query
    /*
        SELECT * FROM Participants_TABLE WHERE Meet_Id='' AND Event1='' OR Event2='' OR Event3='' OR Event4=''" 
    */

    public DatabaseOperations(Context context) //default constructor
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("*Database operations", "Database instance created");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_Profile_TABLE); //execute create table query
        db.execSQL(CREATE_Meet_TABLE); //execute create table query
        db.execSQL(CREATE_Participants_TABLE); //execute create table query
        Log.d("*Database operations", "Profile Table created!");
        Log.d("*Database operations", "Meet Table created!");
        Log.d("*Database operations", "Participants Table created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public String insertProfile(SQLiteDatabase db, String name, String gender, String grade, String school) //INSERT
    {
        //Get number of profiles in database
        Cursor cursor = db.rawQuery("SELECT * FROM Profile_TABLE", null);
        if(cursor.getCount() == 10) //if 10 profiles already exist
            return "Sorry maximum number of profiles reached";

        //Check if this swimmer already exists
        if (cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                String name2 = cursor.getString(cursor.getColumnIndex("Name"));
                if(name2.equalsIgnoreCase(name) == true) //if name already exists in table (regardless of case)
                    return "Sorry this profile already exists";
                cursor.moveToNext(); // Move to next row retrieved
            }
        }
        String query = "INSERT INTO Profile_TABLE (Name, School, Gender, Grade) VALUES ('"+name+"', '"+school+"', '"+gender+"', '"+grade+"')";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("*Query error!", "INSERT FAILED");
            return "Sorry, an error occurred.. Please try again.";
        }
        Log.d("*Database operations", "One row inserted!");
        return "Success";
    }

    public String updateProfile(SQLiteDatabase db, String name, String school, String gender, String grade) //UPDATE
    {
        ContentValues cv = new ContentValues();
        cv.put("School", school);
        cv.put("Gender", gender);
        cv.put("Grade", grade);
        try {
            db.update("Profile_TABLE", cv, "Name='"+name+"'", null);
        }catch (Exception e){
            Log.e("*Query error!", "UPDATE FAILED");
            return "Sorry, an error occurred.. Please try again.";
        }
        Log.d("*Database operations", "One row updated!");
        return "Success";
    }

    public void deleteProfile(SQLiteDatabase db, String name) //DELETE
    {
        String query = "DELETE FROM Profile_TABLE WHERE Name='"+name+"'";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("*Query error!", "DELETE FAILED");
        }
        Log.d("*Database operations", "One row deleted (Name='"+name+"'!");
    }
}
