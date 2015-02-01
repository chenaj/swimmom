package swim.swimmom;

import android.view.View;

/**
 * Created by Smooth on 1/31/2015.
 */
public class RumbleAction {

    public RumbleAction(View v)
    {
        v.performHapticFeedback(9, 11);
    }

}
