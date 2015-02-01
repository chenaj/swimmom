package swim.swimmom;

import android.content.ContentValues;
import android.content.Context;
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

    // profile_table columns
    public static final String SID = "Id";
    public static final String NAME = "Name";
    public static final String GENDER = "Gender";
    public static final String GRADE = "Grade";
    public static final String SCHOOL = "School";

    /*public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+SID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+NAME+" VARCHAR(50), "+GENDER+" VARCHAR(6), "+GRADE+" VARCHAR(9), "+SCHOOL+" VARCHAR(100));";*/

    public static final String CREATE_TABLE =
            "CREATE TABLE Profile_TABLE" +
            "(" +
            "Id int," +
            "Name varchar(60)," +
            "Gender varchar(6)," +
            "Grade varchar(9)," +
            "School varchar(100)" +
            ");";

    public DatabaseOperations(Context context) //default constructor
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database operations", "Database created bro!");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        Log.d("Database operations", "Table about to be created!");
        db.execSQL(CREATE_TABLE); //execute create table query
        Log.d("Database operations", "Profile Table created bro!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Profile_TABLE");
        db.execSQL(CREATE_TABLE); //execute create table query
    }

    public void putInformation(DatabaseOperations dop,String NAME,String GENDER, String GRADE, String SCHOOL, String SWIM_ID)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, NAME);
        cv.put(GENDER, GENDER);
        cv.put(GRADE, GRADE);
        cv.put(SCHOOL, SCHOOL);
        cv.put(SID, SWIM_ID);
        long k= SQ.insert(TABLE_NAME, null,cv);
        Log.d("Database operations", "One Row inserted Bro!");
    }
}

/*         First create table query
        String CREATE_QUERY = "CREATE TABLE " + TableInfo.TABLE_NAME +"("+
        TableInfo.NAME + " VARCHAR(60)," +
        TableInfo.GENDER + "VARCHAR(6)" +
        TableInfo.GRADE + "VARCHAR(9)" +
        TableInfo.SCHOOL + "VARCHAR(100)" +
        TableInfo.SWIM_ID + "INT(2) );";*/