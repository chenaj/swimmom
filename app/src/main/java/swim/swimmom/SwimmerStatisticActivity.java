package swim.swimmom;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by wajihaf56 on 2/24/2015.
 */
public class SwimmerStatisticActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_swim);
        new MyActionBar(getSupportActionBar(), "Swimmer Statistics");
    }

}
