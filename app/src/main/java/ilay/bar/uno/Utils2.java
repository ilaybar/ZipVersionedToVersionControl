package ilay.bar.uno;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

public class Utils2 {

    static boolean handleMainMenu(MenuItem item, Activity activity)
    {
        int resID = item.getItemId();
        switch(resID){
            case R.id.item1:
                Intent settings = new Intent(activity, SettingsActivity.class);
                settings.putExtra("key", ""); //Optional parameters
                activity.startActivity(settings);
                return true;
            case R.id.item2:
                Intent instructions = new Intent(activity, InstructionsActivity.class);
                instructions.putExtra("key", ""); //Optional parameters
                activity.startActivity(instructions);
                return true;
        }
        return false;
    }

}
