package swim.swimmom;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Smooth on 1/31/2015.
 */
public class MessagePrinter {
    public MessagePrinter(Context context, String message)
    {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        v.setTextSize(20);
        toast.show();
    }
}
