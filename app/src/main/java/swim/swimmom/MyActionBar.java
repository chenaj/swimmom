package swim.swimmom;

import android.graphics.drawable.ColorDrawable;
import android.text.Html;

/**
 * Created by Smooth on 2/15/2015.
 */
public class MyActionBar {

    public MyActionBar(android.support.v7.app.ActionBar actionBar, String title)
    {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff0088aa)); // Action bar color
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">"+title+"</font>"));
    }
}
