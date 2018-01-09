package com.aia.utility;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class App extends MultiDexApplication
{
    public static final String TAG = App.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static App mInstance;
    public ArrayList<String> permissions;
    private static Typeface fontRegular, fontBold, fontItalic;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.READ_SMS);

        fontRegular = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Neris_Regular.otf");
        fontBold = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Neris_Bold.otf");
        fontItalic = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Neris_Italic.otf");

        initPicassoSetup();
    }

    public static Typeface getFontRegular() { return fontRegular; }

    public static Typeface getFontBold() { return fontBold; }

    public static Typeface getFontItalic() { return fontItalic; }

    public static synchronized App getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void initPicassoSetup()
    {
        //Setup Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        builder.defaultBitmapConfig(Bitmap.Config.RGB_565);
        Picasso.setSingletonInstance(built);
    }

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
