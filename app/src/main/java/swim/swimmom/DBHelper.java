package swim.swimmom;

/**
 * Created by Angela on 1/30/2015.
 */

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Hashtable;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.DatabaseUtils;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.database.sqlite.SQLiteDatabase;

    public class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "SwimMom.db";
        public static final String PROFILE_TABLE_NAME = "profile";
        public static final String PROFILE_COLUMN_ID = "id";
        public static final String PROFILE_COLUMN_NAME = "name";
        public static final String PROFILE_COLUMN_GENDER = "gender";
        public static final String PROFILE_COLUMN_SCHOOL = "school";
        public static final String PROFILE_COLUMN_GRADE = "grade";

        private HashMap hp;

        public DBHelper(Context context)
        {
            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(
                    "create table profile " +
                            "(id integer primary key, name text,gender text,grade text, school text)"
                            //"(id integer primary key, name text,email text,email text, street text,place text)"

            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }

        public boolean insertProfile  (String name, String gender, String grade, String school)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("name", name);
            contentValues.put("gender", gender);
            contentValues.put("grade", grade);
            contentValues.put("school", school);

            db.insert("profile", null, contentValues);
            return true;
        }
        public Cursor getData(int id){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from profile where id="+id+"", null );
            return res;
        }
        public int numberOfRows(){
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, PROFILE_TABLE_NAME);
            return numRows;
        }
        public boolean updateProfile (Integer id, String name, String gender, String grade, String school)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("gender", gender);
            contentValues.put("grade", grade);
            contentValues.put("school", school);
        /*    contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("street", street);
            contentValues.put("place", place);*/
            db.update("profile", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            return true;
        }

        public Integer deleteProfile (Integer id)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete("profile",
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }
        public ArrayList getAllProfiles()
        {
            ArrayList array_list = new ArrayList();
            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from contacts", null );
            res.moveToFirst();
            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(PROFILE_COLUMN_NAME)));
                res.moveToNext();
            }
            return array_list;
        }
    }

