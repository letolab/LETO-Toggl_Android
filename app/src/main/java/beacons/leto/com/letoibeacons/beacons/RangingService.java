package beacons.leto.com.letoibeacons.beacons;

/**
 * Created by Renzo on 27/11/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;


public class RangingService extends Service implements BeaconConsumer {



    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("RANGING", "Ranging destroyed");
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d("RANGING", "Beacon connected");

    }
}
