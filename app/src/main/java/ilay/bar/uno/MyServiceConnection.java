package ilay.bar.uno;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {
    private MusicService myService; // Instance of MusicService
    private boolean isServiceBound = false; // Flag to check if the service is bound

    // This method is called when the Activity successfully connects to the MyService class
    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        // Cast the IBinder and get the MyService instance
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        myService = binder.getService();
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        myService = null;
        isServiceBound = false;
    }

    public MusicService getService() {
        return myService;
    }

    public boolean isServiceBound() {
        return isServiceBound;
    }
}
