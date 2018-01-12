package com.aia.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aia.R;
import com.aia.models.AssetDetailModel;


public class DialogCreator
{
    public static void showMessageDialog(Context mContext, String message, String mImageDisplay)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
        Typeface fontRegular = App.getFontRegular();
        ImageView imageView = promptView.findViewById(R.id.image_view);
        if(mImageDisplay.equals(CommonUtility.getString(mContext, R.string.error)))
            imageView.setImageResource(R.drawable.ic_action_warning);
        else
            imageView.setImageResource(R.drawable.ic_action_success);

        TextView msg = promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(fontRegular);
        msg.setText(message);
        Button ok = promptView.findViewById(R.id.btn_ok);
        ok.setTypeface(fontRegular);
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
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title_sub_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        Typeface fontRegular = App.getFontRegular(), fontItalic = App.getFontItalic();
        TextView txtTitle = promptView.findViewById(R.id.tv_title);
        txtTitle.setTypeface(fontItalic);
        txtTitle.setText(title);
        TextView txtSubTitle = promptView.findViewById(R.id.tv_msg);
        txtSubTitle.setTypeface(fontRegular);
        txtSubTitle.setText(message);
        Button yes = promptView.findViewById(R.id.btn_yes);
        yes.setTypeface(fontRegular);
        yes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                activity.finish();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(a);
                alert.dismiss();
            }
        });

        Button no = promptView.findViewById(R.id.btn_no);
        no.setTypeface(fontRegular);
        no.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.show();
    }

   /* public static void showLogoutDialog(final Activity activity, String title, String message)
    {
        Typeface regular = App.getSansationRegularFont();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        TextView t= promptView.findViewById(R.id.tv_title);
        t.setTypeface(regular);
        t.setText(title);
        TextView msg = promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(regular);
        msg.setText(message);
        Button yes = promptView.findViewById(R.id.btn_yes);
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

        Button no = promptView.findViewById(R.id.btn_no);
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

    public static void showAssetDetailsDialog(final Context context, AssetDetailModel assetDetailModel)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_assets_details, null);
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        Typeface fontRegular = App.getFontRegular(), fontBold = App.getFontBold();

        TextView lblAssetId, lblAssetCategory, lblAssetSubCategory;
        TextView txtAssetName, txtAssetMakeNo, txtAssetId, txtAssetCategory, txtAssetSubCategory;

        //Initialising all labels starts
        lblAssetId = promptView.findViewById(R.id.lbl_asset_id);
        lblAssetId.setTypeface(fontBold);
        lblAssetCategory = promptView.findViewById(R.id.lbl_assets_category);
        lblAssetCategory.setTypeface(fontBold);
        lblAssetSubCategory = promptView.findViewById(R.id.lbl_subcategory);
        lblAssetSubCategory.setTypeface(fontBold);

        txtAssetName = promptView.findViewById(R.id.txt_asset_name);
        txtAssetName.setTypeface(fontBold);
        txtAssetMakeNo = promptView.findViewById(R.id.txt_make_make_no);
        txtAssetMakeNo.setTypeface(fontRegular);
        txtAssetId = promptView.findViewById(R.id.txt_asset_id);
        txtAssetId.setTypeface(fontRegular);
        txtAssetCategory = promptView.findViewById(R.id.txt_category);
        txtAssetCategory.setTypeface(fontRegular);
        txtAssetSubCategory = promptView.findViewById(R.id.txt_subcategory);
        txtAssetSubCategory.setTypeface(fontRegular);


        //Setting values to the text starts
        txtAssetName.setText(assetDetailModel.assetName);
        txtAssetMakeNo.setText(assetDetailModel.assetMake + "|" + assetDetailModel.assetMakeNo);
        txtAssetId.setText(assetDetailModel.assetId);
        txtAssetCategory.setText(assetDetailModel.assetCategory);
        txtAssetSubCategory.setText(assetDetailModel.assetSubCategory);

        //Setting values to the text ends

        //OK button code starts
        Button yes = promptView.findViewById(R.id.btn_ok);
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
