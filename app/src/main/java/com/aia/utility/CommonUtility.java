package com.aia.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.aia.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CommonUtility
{

    private static CommonUtility nUtilHelper;
    private static Context mContext;
   public static ProgressDialog pDialog;

    public static float density = 1;

    public static CommonUtility getInstance(Context context)
    {
        if (nUtilHelper == null)
        {
            mContext = context;
            nUtilHelper = new CommonUtility();
        }
        return nUtilHelper;
    }

    public boolean isValidEmail(CharSequence target)
    {
        if (target == null)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidNumber(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public boolean checkConnectivity(Context pContext)
    {
        ConnectivityManager lConnectivityManager = (ConnectivityManager)
                pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo lNetInfo = lConnectivityManager.getActiveNetworkInfo();
        return lNetInfo != null && lNetInfo.isConnected();
    }

    public static void hideKeyBoard(Context context)
    {
        View view = ((Activity)context).getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static int getColor(Context context, int id)
{
    final int version = Build.VERSION.SDK_INT;
    if (version >= 23)
    {
        return ContextCompat.getColor(context, id);
    }
    else
    {
        return context.getResources().getColor(id);
    }
}

    @TargetApi(Build.VERSION_CODES.M)
    public static void askForPermissions(final Context context, ArrayList<String> permissions) {
        ArrayList<String> permissionsToRequest = findUnAskedPermissions(context, App.getInstance().permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        final ArrayList<String> permissionsRejected = findRejectedPermissions(context, App.getInstance().permissions);

        if(permissionsToRequest.size()>0)
        {
            //we need to ask for permissions
            //but have we already asked for them?

            ((Activity)context).requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    AppConstants.ALL_PERMISSIONS_RESULT);


            //mark all these as asked..
            /*for(String perm : permissionsToRequest){
                markAsAsked(context,perm);
            }*/
        }
        else
        {

            if(permissionsRejected.size()>0)
            {
                //we have none to request but some previously rejected..tell the user.
                //It may be better to show a dialog here in a prod application
//                showPostPermissionsShackBar(context, ll_main_view, permissionsRejected);
            }
        }
    }

   /* public static void showPostPermissionsShackBar(final Context context, RelativeLayout ll_mail_view, final ArrayList<String> permissionsRejected) {
        Snackbar snackBarView = Snackbar
                .make(ll_mail_view, String.valueOf(permissionsRejected.size()) + "SnakeBar", Snackbar.LENGTH_LONG)
                .setAction("SnakeBar", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        for (String perm : permissionsRejected)
                        {
                            clearMarkAsAsked(context, perm);
                        }
                    }
                });

        ViewGroup group = (ViewGroup) snackBarView.getView();
        group.setBackgroundColor(getColor(context, R.color.colorPrimary));
        snackBarView.show();
    }*/


    /**
     * method that will return whether the permission is accepted. By default it is true if the user is using a device below
     * version 23
     * @param context
     * @param permission
     * @return
     */

    public static boolean hasPermission(Context context, String permission)
    {
        if (canMakeSmores())
        {
            return(ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }



    /**
     * we will save that we have already asked the user
     * @param permission
     */
    public static void markAsAsked(Context context, String permission)
    {
        AppPreferences.saveValue(context,permission, false);
    }

    /**
     * We may want to ask the user again at their request.. Let's clear the
     * marked as seen preference for that permission.
     * @param permission
     */
    public static void clearMarkAsAsked(Context context, String permission)
    {
        AppPreferences.saveValue(context,permission, true);
    }


    /**
     * This method is used to determine the permissions we do not have accepted yet and ones that we have not already
     * bugged the user about.  This comes in handle when you are asking for multiple permissions at once.
     * @param wanted
     * @return
     */
    public static ArrayList<String> findUnAskedPermissions(Context context, ArrayList<String> wanted)
    {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted)
        {
            if (!hasPermission(context,perm) && AppPreferences.shouldWeAskPermission(context,perm))
            {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * this will return us all the permissions we have previously asked for but
     * currently do not have permission to use. This may be because they declined us
     * or later revoked our permission. This becomes useful when you want to tell the user
     * what permissions they declined and why they cannot use a feature.
     * @param wanted
     * @return
     */
    public static ArrayList<String> findRejectedPermissions(Context context, ArrayList<String> wanted)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted)
        {
            if (!hasPermission(context,perm) && !AppPreferences.shouldWeAskPermission(context,perm))
            {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * Just a check to see if we have marshmallows (version 23)
     * @return
     */
    private static boolean canMakeSmores()
    {
        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static int dp(float value) {
        return (int)Math.ceil(density * value);
    }

    public static void setAnimation(View viewToAnimate, int position, int lastPosition, Context mContext)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static String getString(Context context, @StringRes int resId)
    {
        return context.getResources().getString(resId);
    }

    /**
     * Here if condition check for WiFi and Mobile network is available or not.
     * If anyone of them is available or connected then it will return true,
     * otherwise false.
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager conn_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network_info = conn_manager.getActiveNetworkInfo();

        if (network_info != null && network_info.isConnected()) {
            if (network_info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            else if (network_info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }

        return false;
    }

    public static String getCurrentDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static Bitmap addWaterMarkDate(Bitmap src, String watermark)
    {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        //paint.setAlpha(alpha);
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawText(watermark, 5, h-5, paint);

        return result;
    }

    public static String encodeToString(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getPreviousDate(int prev){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -prev);
        Date newDate = calendar.getTime();
        return dateFormat.format(newDate);
    }

}
