package swim.swimmom;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by wajihaf56 on 2/10/2015.
 */
public class AddCutTimes extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_time_add);
        new MyActionBar(getSupportActionBar(), "Add Cut Time"); // Create action bar
    }
}
