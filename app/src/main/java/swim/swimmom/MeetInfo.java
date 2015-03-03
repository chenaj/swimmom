package swim.swimmom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smooth on 2/21/2015.
 */
public class MeetInfo {
    public static String opponent = "";
    public static String location = "";
    public static String date = "";
    public static String time = "";

    //2d array to hold swimmers and their event info
    public static ArrayList<List<String>> swimmers = new ArrayList<List<String>>();
    /* Example of array 'swimmers' contents:
    NAME            EVENT1                  EVENT2                   EVENT3                  EVENT4
    Swimmer1  |   200 yd. Medley Relay |  50 yd. Freestyle     |   100 yd. Freestyle   |    400 yd. Freestyle Relay
    Swimmer2  |   100 yd. Freestyle    |     _                 |   _                   |    _
    Swimmer3  |   200 yd. Freestyle    |  100 yd. Breaststroke |   _                   |    _
     */
    public static void clearInfo() //reset meet info
    {
        opponent = "";
        location = "";
        date = "";
        time = "";
        swimmers.clear();
    }
}
