package me.tombailey.downloadovertor;

import android.app.Application;

import me.tombailey.downloadovertor.service.TorService;

/**
 * Created by Tom on 25/09/2016.
 */

public class App extends Application {

    private TorService mTorService;

    @Override
    public void onCreate() {
        super.onCreate();

        mTorService = new TorService(this, "tor");
    }

    public TorService getTorService() {
        return mTorService;
    }


}
