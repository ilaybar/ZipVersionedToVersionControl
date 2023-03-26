package ilay.bar.uno;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;

public class Globals {

    public static String PlayersKey = "artistsStr";
    public static String PrefName = "MyPrefs";

    public static MyServiceConnection connection = new MyServiceConnection(); // MediaPlayer Service

}
