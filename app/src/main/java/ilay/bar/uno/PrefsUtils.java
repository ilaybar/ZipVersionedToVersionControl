package ilay.bar.uno;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefsUtils
{
    public static void writePlayersList(ArrayList<Player> players, SharedPreferences.Editor editor, String key)
    {
        // Convert arrayList to Json
        Gson gson = new Gson();
        String json = gson.toJson(players);
        Log.d("WritePersonsList", json);
        // Save it to prefs
        editor.putString(key, json);
        editor.commit();
    }

    public static ArrayList<Player> readPlayersList(SharedPreferences pref, String key)
    {
        Gson gson = new Gson();
        // Read json string from prefs
        String jsonStr = pref.getString(key, "");
        // Build the TypeToken for parsing  ArrayList<Person>
        Type arraylistType = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> personArrList = new ArrayList<Player>(); // will store read data

        try
        {
            personArrList = gson.fromJson(jsonStr, arraylistType);
        }
        catch (Exception e)
        {
            Log.e("ReadJsonArrayList", "Error decoding json: " + jsonStr);
            e.printStackTrace();
            return null;
        }
        return personArrList;
    }

}
