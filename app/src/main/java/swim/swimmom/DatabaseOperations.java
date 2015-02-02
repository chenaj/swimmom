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
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "Profile_TABLE";

    // profile_table column headers
    public static final String SID = "Id";
    public static final String NAME = "Name";
    public static final String SCHOOL = "School";
    public static final String GENDER = "Gender";
    public static final String GRADE = "Grade";

    public static final String CREATE_TABLE =
            "CREATE TABLE Profile_TABLE" +
            "(" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Name varchar(60)," +
            "School varchar(100)," +
            "Gender varchar(6)," +
            "Grade varchar(9)" +
            ");";

    public DatabaseOperations(Context context) //default constructor
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("***** Database operations", "Database created bro!");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE); //execute create table query
        Log.d("Database operations", "Profile Table created bro!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public Boolean insertProfile(SQLiteDatabase db,String name,String gender, String grade, String school)
    {
        //Check if this swimmer already exists
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Name= '"+name+"'",null);
        if(cursor.getCount() > 0) //if a profile with same name already exists
            return false;

        String query = "INSERT INTO "+TABLE_NAME+" (Name, School, Gender, Grade) VALUES ('"+name+"', '"+school+"', '"+gender+"', '"+grade+"')";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.e("Query error!", "INSERT FAILED");
        }

        Log.d("*Database operations", "One Row inserted Bro!");
        Log.d("*Name", name);
        Log.d("*School", school);
        Log.d("*Gender", gender);
        Log.d("*Grade", grade);

        return true;
    }
}
