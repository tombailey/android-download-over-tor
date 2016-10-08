package me.tombailey.downloadovertor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import me.tombailey.downloadovertor.service.TorService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TorService torService = ((App) getApplication()).getTorService();
        torService.start()
                .flatMap(new Func1<Boolean, Observable<me.tombailey.tor.Response>>() {
                    @Override
                    public Observable<me.tombailey.tor.Response> call(Boolean running) {
                        if (!running) {
                            throw new RuntimeException("tor service is not running");
                        }

                        try {
                            return torService.request(new me.tombailey.tor.Request(me.tombailey.tor.Request.Method.GET, new URL("http://ip-api.com/json"), null));
                        } catch (MalformedURLException mue) {
                            throw new RuntimeException(mue);
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<me.tombailey.tor.Response>() {
                    @Override
                    public void call(me.tombailey.tor.Response response) {
                        Log.d(LOG_TAG, String.valueOf(response.getStatusCode()));
                        try {
                            Log.d(LOG_TAG, response.getBodyAsString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
