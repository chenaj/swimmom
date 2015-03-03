package swim.swimmom;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Smooth on 3/3/2015.
 */
public class CutInfo {
    public static ArrayList<HashMap<String, String>> cutInfo = new ArrayList<>();
    public static String cutName = "";
    public static String gender = "";

    public static void clearInfo() //reset meet info
    {
        cutName = "";
        gender = "";
        cutInfo.clear();
    }

}
