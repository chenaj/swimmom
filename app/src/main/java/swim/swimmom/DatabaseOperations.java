package swim.swimmom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by wajihaf56 on 1/30/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper{

    //** variable represents name in code, the string represents column name in table
    public static final String DATABASE_NAME = "swimmom.db";
    public static final int DATABASE_VERSION = 6;

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
                    "Time varchar(10)," +
                    "Opponent varchar(100)" +
                    ");";

    public static final String CREATE_Participants_TABLE =
            "CREATE TABLE Participants_TABLE" +
                    "(" +
                    "Meet_Id varchar(4)," +
                    "Name varchar(60)," +
                    "Event varchar(50)" +
                    /*
                    "PRIMARY KEY (Meet_Id),"+
                    "FOREIGN KEY (Meet_Id) REFERENCES Meet_TABLE(Meet_Id)"+
                    */
                    ");";

    public static final String CREATE_Statistics_TABLE =
            "CREATE TABLE Statistics_TABLE" +
                    "(" +
                    "Name varchar(60)," +
                    "Event varchar(50)," +
                    "Event_Time varchar(6)," +
                    "Date date NOT NULL," +
                    "Meet_Id INTEGER" +
                    /*
                    "PRIMARY KEY (Name),"+
                    "FOREIGN KEY (Meet_Id) REFERENCES Meet_TABLE(Meet_Id)"+
                    */
                    ");";

    //Stores names of all cuts created by user
    public static final String CREATE_BoysCuts_TABLE =
            "CREATE TABLE BoysCuts_TABLE" +
                    "(" +
                    "Name varchar(200) PRIMARY KEY" +
                    ");";
    public static final String CREATE_GirlsCuts_TABLE =
            "CREATE TABLE GirlsCuts_TABLE" +
                    "(" +
                    "Name varchar(200) PRIMARY KEY" +
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
        db.execSQL(CREATE_Meet_TABLE);
        db.execSQL(CREATE_Participants_TABLE);
        db.execSQL(CREATE_Statistics_TABLE);
        db.execSQL(CREATE_BoysCuts_TABLE);
        db.execSQL(CREATE_GirlsCuts_TABLE);
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
            while (!cursor.isAfterLast())
            {
                String name2 = cursor.getString(cursor.getColumnIndex("Name"));
                if(name2.equalsIgnoreCase(name)) //if name already exists in table (regardless of case)
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
            Log.e("*Query error!", "DELETE PROFILE FAILED");
            return;
        }
        Log.d("*Database operations", "One row deleted (Name='"+name+"'!");
    }

    public String insertMeet(SQLiteDatabase db, String opponent, String location, String date, String time) //INSERT meet
    {
        Cursor cursor = db.rawQuery("SELECT * FROM Meet_TABLE WHERE date='"+date+"'", null);
        if(cursor.getCount() > 0) //if a meet already exists on this day
            return "Sorry there is already a meet created for this date";

        String query = "INSERT INTO Meet_TABLE (Opponent, Location, Date, Time) VALUES ('"+opponent+"', '"+location+"', '"+date+"', '"+time+"')";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("*Query error!", "INSERT FAILED");
            return "Sorry, an error occurred.. Please try again.";
        }
        Log.d("*Database operations", "One row inserted in meet table!");
        Log.d("Opponent", opponent);
        Log.d("Location", location);
        Log.d("Date", date);
        Log.d("Time", time);
        return "Success";
    }

    public void deleteMeet(SQLiteDatabase db, String meetid) //DELETE
    {
        String query = "DELETE FROM Meet_TABLE WHERE Meet_Id='"+meetid+"'";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("*Query error!", "DELETE MEET FAILED");
            return;
        }
        Log.d("*Database operations", "One row deleted (Meet_Id='"+meetid+"'!");
    }

    public String insertParticipants(SQLiteDatabase db, String meetid, String name, String event) //INSERT meet
    {
        String query = "INSERT INTO Participants_TABLE (Meet_Id, Name, Event) VALUES ('"+meetid+"', '"+name+"', '"+event+"')";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("*Query error!", "INSERT FAILED");
            return "Sorry, an error occurred.. Please try again.";
        }
        Log.d("*Database operations", "One row inserted in participants table!");
        Log.d("Meet_Id", ""+meetid+"");
        Log.d("Name", name);
        Log.d("Event", event);
        return "Success";
    }

    public String insertCut(SQLiteDatabase db, String cutName, String gender, ArrayList<HashMap<String, String>> cutList) //INSERT cut
    {
        // example cut name: "Wayne County"
        // example of corresponding table name: "wayne_county_boys" or "wayne_county_girls"
        // i.e., gender is appended to end of cut name
        cutName = cutName.trim().replaceAll(" +", "_"); //replace single or consecutive spaces with single underscore
        String CREATE_Cut_TABLE =
                "CREATE TABLE "+cutName+"" +
                        "(" +
                        "Event varchar(100)," +
                        "Time varchar(8)" +
                        ");";
        Log.d("Gender", gender);
        Log.d("Table Name", cutName);

        db.execSQL(CREATE_Cut_TABLE); //create new table for this cut

        String query, event, time;
        for(int row=0; row < cutList.size(); row++)
        {
            event = cutList.get(row).get("Event");
            time = cutList.get(row).get("Time");
            query  = "INSERT INTO "+cutName+" (Event, Time) VALUES ('" + event + "', '" + time + "')";
            try {
                db.execSQL(query);
            } catch (Exception e) {
                Log.e("*Query error!", "INSERT FAILED");
                return "Sorry, an error occurred.. Please try again.";
            }
            Log.d("Event & Time", ""+event+" = "+time+"");
        }

        if(gender.equals("Boys")) {
            query = "INSERT INTO BoysCuts_TABLE (Name) VALUES('" + cutName + "')";
            Log.d("Cut stored in ","Boys table");
        }
        else {
            query = "INSERT INTO GirlsCuts_TABLE (Name) VALUES('" + cutName + "')";
            Log.d("Cut stored in ","Girls table");
        }

        db.execSQL(query); //create new table for this cut
        return "Success";
    }

    public String updateCut(SQLiteDatabase db, String cutName, String gender, ArrayList<HashMap<String, String>> cutList) //UPDATE cut
    {
        String query, event, time;
        for(int row=0; row < cutList.size(); row++)
        {
            event = cutList.get(row).get("Event");
            time = cutList.get(row).get("Time");
            query  = "UPDATE "+cutName+" SET Time='"+time+"' WHERE Event='"+event+"'";
            try {
                db.execSQL(query);
            } catch (Exception e) {
                Log.e("*Query error!", "UPDATE FAILED");
                return "Sorry, an error occurred.. Please try again.";
            }
            Log.d("Event & Time", ""+event+" = "+time+"");
        }
        return "Success";
    }

    public String deleteCut(SQLiteDatabase db, String cutName, String gender) //UPDATE cut
    {
        String query;
        if(gender.equals("Boys"))
            query  = "DELETE FROM BoysCuts_TABLE WHERE Name='"+cutName+"''";
        else
            query  = "DELETE FROM GirlsCuts_TABLE WHERE Name='"+cutName+"''";

        try {
            db.execSQL(query);
        } catch (Exception e) {
            Log.e("*Query error!", "DELETE FAILED");
            return "Sorry, an error occurred.. Please try again.";
        }
        Log.d("Cut Deleted", cutName);

        return "Success";
    }
}
