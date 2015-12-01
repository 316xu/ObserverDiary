package hust.xujifa.observerdiary.helper;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by xujifa on 2015/8/4.
 */
public class MyApp extends Application {

    public static final String TAG=MyApp.class.getSimpleName();

    private RequestQueue requestQueue;
    private static MyApp instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

    }

    public static synchronized MyApp getInstance(){
        return instance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue=Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T>void addToQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public <T>void addToQueue(Request<T>req, String tag){
        req.setTag(TextUtils.isEmpty(tag)?tag:TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
