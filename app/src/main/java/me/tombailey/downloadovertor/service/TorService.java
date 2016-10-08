package me.tombailey.downloadovertor.service;

import android.content.Context;

import com.msopentech.thali.android.toronionproxy.AndroidOnionProxyManager;
import com.msopentech.thali.toronionproxy.OnionProxyManager;

import java.io.IOException;

import me.tombailey.tor.Request;
import me.tombailey.tor.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Tom on 25/09/2016.
 */

public class TorService {

    private OnionProxyManager mOnionProxyManager;

    public TorService(Context context, String cacheSubDirectory) {
       mOnionProxyManager = new AndroidOnionProxyManager(context, cacheSubDirectory);
    }

    public Observable<Boolean> start() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean started = mOnionProxyManager.startWithRepeat(240, 5);
                    mOnionProxyManager.enableNetwork(true);
                    subscriber.onNext(started && mOnionProxyManager.isRunning());
                    subscriber.onCompleted();
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Response> request(final Request request) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    subscriber.onNext(request.request(mOnionProxyManager));
                    subscriber.onCompleted();
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }
            }
        });
    }

    public Observable<Boolean> isRunning() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean isRunning = mOnionProxyManager != null && mOnionProxyManager.isRunning();
                    subscriber.onNext(isRunning);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public void stop() throws IOException {
        mOnionProxyManager.stop();
    }

}
