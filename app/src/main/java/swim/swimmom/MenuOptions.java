package swim.swimmom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Angela on 2/15/2015.
 */
public class MenuOptions {

    public boolean MenuOption(View curView, MenuItem item, Context context, Class parentPage)
    {
        new RumbleAction(curView);
        // Handle item selection
        Intent intent;
        DatabaseOperations obj = new DatabaseOperations(context);
        boolean isThereProfiles = false;

        //Checks if there is any profiles on app
        if(obj.numContent() != 0)
            isThereProfiles = true;

    switch (item.getItemId()) {
        case android.R.id.home:
            //onBackPressed();
            new RumbleAction(curView);
            intent = new Intent(context, parentPage);
            context.startActivity(intent);
            return true;


        case R.id.mainOption:
            new RumbleAction(curView);
            intent = new Intent (context, MainActivity.class);
            context.startActivity(intent);
            return true;

        case R.id.profilesOption:
            new RumbleAction(curView);
            intent = new Intent (context, ProfileActivity.class);
            context.startActivity(intent);
            return true;

        case R.id.meetsOption:
            new RumbleAction(curView);
        if(isThereProfiles){
            intent = new Intent (context, MeetActivity.class);
            context.startActivity(intent);
            return true;
        }else {
            new MessagePrinter().longMessage(context, "Please create a profile first");
            return false;
        }

        case R.id.cutTimesOption:
            new RumbleAction(curView);
            intent = new Intent (context, CutTimeActivity.class);
            context.startActivity(intent);
            return true;

        case R.id.statisticsOption:
            new RumbleAction(curView);
            if(isThereProfiles) {
                intent = new Intent(context, StatisticActivity.class);
                context.startActivity(intent);
                return true;
            }else {
                new MessagePrinter().longMessage(context, "Please create a profile first");
                return false;
            }
        default:
            return false;
    }
    }
}


