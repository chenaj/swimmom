package swim.swimmom;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import swim.swimmom.TableData.TableInfo;


/**
 * Created by wajihaf56 on 1/30/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper{

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableInfo.TABLE_NAME +
                    "("+ TableInfo.NAME + " VARCHAR(50)," +
                    TableInfo.GENDER + "VARCHAR(6)" +
                    TableInfo.GRADE + "VARCHAR(9)" +
                    TableInfo.SCHOOL + "VARCHAR(100)" +
                    TableInfo.SWIM_ID + "VARCHAR(2) );";

    public DatabaseOperations(Context context) {

        super(context, TableInfo.DATABASE_NAME, null, database_version);
        //Log.d("Database operations", "Database created bro!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created bro!");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
     public void putInformation(DatabaseOperations dop,String NAME,String GENDER, String GRADE, String SCHOOL, String SWIM_ID)
     {

         SQLiteDatabase SQ = dop.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(TableInfo.NAME, NAME);
         cv.put(TableInfo.GENDER, GENDER);
         cv.put(TableInfo.GRADE, GRADE);
         cv.put(TableInfo.SCHOOL, SCHOOL);
         cv.put(TableInfo.SWIM_ID, SWIM_ID);
         long k= SQ.insert(TableInfo.TABLE_NAME, null,cv);
         Log.d("Database operations", "One Row inserted Bro!");



     }
}
