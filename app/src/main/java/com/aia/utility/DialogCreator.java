package com.aia.utility;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aia.R;


public class DialogCreator
{
    // Created By Prachi 20/12/17
  /*  public static void showMessageDialog(Context mContext, String message, String mImageDisplay)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.dialog_without_title, null);
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
        ImageView imageView = (ImageView)promptView.findViewById(R.id.image_view);
        if(mImageDisplay.equals("error"))
        {
            imageView.setImageResource(R.drawable.high_importance);
        }
        else
        {
            imageView.setImageResource(R.drawable.checked_green);
        }
        TextView msg = (TextView) promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(regular);
        msg.setText(message);
        Button ok = (Button) promptView.findViewById(R.id.btn_ok);
        ok.setTypeface(regular);
        ok.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.show();
    }



    public static void showExitDialog(final Activity activity, String title, String message, final String screenName)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        TextView t= (TextView) promptView.findViewById(R.id.tv_title);
        t.setTypeface(regular);
        t.setText(title);
        TextView msg = (TextView) promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(regular);
        msg.setText(message);
        Button yes = (Button) promptView.findViewById(R.id.btn_yes);
        yes.setTypeface(regular);
        yes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                activity.finish();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(a);
                AppPreferences.getInstance(activity).putString(AppConstants.SCREEN_FROM_EXIT, screenName);
                alert.dismiss();
            }
        });

        Button no = (Button) promptView.findViewById(R.id.btn_no);
        no.setTypeface(regular);
        no.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.show();
    }*/

   /* public static void showLogoutDialog(final Activity activity, String title, String message)
    {
        Typeface regular = App.getSansationRegularFont();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        TextView t= (TextView) promptView.findViewById(R.id.tv_title);
        t.setTypeface(regular);
        t.setText(title);
        TextView msg = (TextView) promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(regular);
        msg.setText(message);
        Button yes = (Button) promptView.findViewById(R.id.btn_yes);
        yes.setTypeface(regular);
        yes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CommonUtils.logout(activity);
                Intent in = new Intent(activity, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(in);
                activity.finish();
//                SharedPreferences settings = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                settings.edit().clear().commit();
                alert.dismiss();
            }
        });

        Button no = (Button) promptView.findViewById(R.id.btn_no);
        no.setTypeface(regular);
        no.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.show();
    }*/

    public static void showAssetDetailsDialog(final Context context, String assetName, String assetCategory, String assetSubCategory, String assetId)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_assets_details, null);
        final AlertDialog alert = new AlertDialog.Builder(context).create();

        TextView lblAssetName, lblAssetid, lblAssetCategory, lblAssetSubCategory;
        TextView txtAssetName, txtAssetId, txtAssetCategory, txtAssetSubCategory;
        //Initialising all labels starts
        txtAssetName = (TextView)promptView.findViewById(R.id.txt_asset_name);
        lblAssetid = (TextView)promptView.findViewById(R.id.lbl_asset_id);
        lblAssetCategory = (TextView)promptView.findViewById(R.id.lbl_assets_category);
        lblAssetSubCategory = (TextView)promptView.findViewById(R.id.lbl_subcategory);
        txtAssetId = (TextView)promptView.findViewById(R.id.txt_asset_id);
        txtAssetCategory = (TextView)promptView.findViewById(R.id.txt_category);
        txtAssetSubCategory = (TextView)promptView.findViewById(R.id.txt_subcategory);


        //Setting values to the text starts
        txtAssetName.setText(assetName);
        txtAssetId.setText(assetId);
        txtAssetCategory.setText(assetCategory);
        txtAssetSubCategory.setText(assetSubCategory);

        //Setting values to the text ends

        //OK button code starts
        Button yes = (Button) promptView.findViewById(R.id.btn_ok);
        yes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();
            }
        });

        alert.setView(promptView);
        alert.show();
        //OK button code ends
    }



}
