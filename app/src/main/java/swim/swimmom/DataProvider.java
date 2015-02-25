package swim.swimmom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Smooth on 2/25/2015.
 */
public class DataProvider {


    public static HashMap<String, List<String>> getInfo(ArrayList swimmerList)
    {
        HashMap<String, List<String>> eventDetails = new HashMap<String, List<String>>();
        List<String> eventList = new ArrayList<String>();

        eventList.add("200 yd. Medley Relay");
        eventList.add("200 yd. Freestyle");
        eventList.add("200 yd. Individual Medley");
        eventList.add("50 yd. Freestyle");
        eventList.add("100 yd. Butterfly");
        eventList.add("100 yd. Freestyle");
        eventList.add("500 yd. Freestyle");
        eventList.add("200 yd. Freestyle Relay");
        eventList.add("100 yd. Backstroke");
        eventList.add("100 yd. Breaststroke");
        eventList.add("400 yd. Freestyle Relay");

        for(int i=0; i<swimmerList.size(); i++)
        {
            eventDetails.put(swimmerList.get(i).toString(), eventList);
        }

        return eventDetails;
    }

}
