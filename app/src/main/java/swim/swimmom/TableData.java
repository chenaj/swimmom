package swim.swimmom;

import android.provider.BaseColumns;

/**
 * Created by wajihaf56 on 1/30/2015.
 */
public class TableData {

    public TableData()
    {

    }

    public static abstract class TableInfo implements BaseColumns
    {
        public static String SWIM_ID = "swim_id";
        public static String NAME = "name";
        public static String GENDER = "gender";
        public static String GRADE = "grade";
        public static String SCHOOL = "school";
        public static String DATABASE_NAME= "swimmomdb";
        public static String TABLE_NAME = "profile_table";

    }

}
