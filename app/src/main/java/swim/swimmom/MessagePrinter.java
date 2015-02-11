package swim.swimmom;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Smooth on 1/31/2015.
 */
public class MessagePrinter {
    public void shortMessage(Context context, String message)
    {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        v.setTextSize(18);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 500);
        toast.show();
    }
    public void longMessage(Context context, String message)
    {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        v.setTextSize(18);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 500);
        toast.show();
    }
}
