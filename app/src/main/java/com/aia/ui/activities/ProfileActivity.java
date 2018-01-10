package com.aia.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.aia.R;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;
import com.aia.webservices.JsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{

    private Context mContext;
    private static int RESULT_LOAD_IMAGE = 1;

    private ImageView imgBack, imgCall, imgLogout, imgCamera;
    private TextView txtFullName, txtLocation, txtId, txtMobileNumber;
    private CircleImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;

        imgBack = findViewById(R.id.img_back);
        imgCall = findViewById(R.id.img_call);
        imgLogout = findViewById(R.id.img_logout);
        imgCamera = findViewById(R.id.img_camera);

        imgBack.setOnClickListener(this);
        imgCall.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        imgCamera.setOnClickListener(this);

        txtFullName = findViewById(R.id.txt_full_name);
        txtLocation = findViewById(R.id.txt_location);
        txtId = findViewById(R.id.txt_id);
        txtMobileNumber = findViewById(R.id.txt_mobile_no);

        imageViewProfile = findViewById(R.id.img_profile);
        setData();
    }

    @Override
    public void onClick(View v)
    {
        if(v == imgBack)
        {
            finish();
        }

        if(v == imgCall)
        {
            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8788610686"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent1);
        }

        if(v == imgLogout)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            final View promptView = layoutInflater.inflate(R.layout.dialog, null);
            final android.app.AlertDialog alert = new android.app.AlertDialog.Builder(this).create();
            TextView msg = promptView.findViewById(R.id.tv_msg);
            ImageView imgDialog = promptView.findViewById(R.id.img_dialog);
            imgDialog.setImageResource(R.drawable.ic_logout);
            TextView title = promptView.findViewById(R.id.tv_title);
            title.setText("You Want to Logout? ");
            msg.setText("Click Ok to continue!");
            Button ok = (Button) promptView.findViewById(R.id.btn_yes);
            ok.setText("OK");

            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    doLogout();
                    alert.dismiss();
                }
            });
            alert.setView(promptView);
            alert.show();

        }

        if(v == imgCamera)
        {
            setImage();
        }
    }

    private void setImage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.select_profile_photo);
        builder.setItems(new CharSequence[]
                        {getString(R.string.gallery), getString(R.string.remove)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                loadImageFromGallery();
                                break;
                            case 1:
                                setDefault();
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    public void setData()
    {
        txtFullName.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, ""));
        txtLocation.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_CITY, ""));
        txtId.setText(AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
        txtMobileNumber.setText(AppPreferences.getInstance(mContext).getString(AppConstants.MOBILE_NO, ""));
        String imgUrl = AppPreferences.getInstance(mContext).getString(AppConstants.PROFILE_IMAGE_URL, "");
    }

    public void doLogout(){
        AppPreferences.getInstance(mContext).putString(AppConstants.AUTH_TOKEN, "");
        AppPreferences.getInstance(mContext).putString(AppConstants.USER_NAME, "");
        AppPreferences.getInstance(mContext).putString(AppConstants.USER_CITY, "");
        AppPreferences.getInstance(mContext).putString(AppConstants.EMP_ID, "");
        AppPreferences.getInstance(mContext).putString(AppConstants.MOBILE_NO, "");
        AppPreferences.getInstance(mContext).putString(AppConstants.PROFILE_IMAGE_URL, "");

        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        LandingActivity.activity.finish();
        finish();
    }
    public void loadImageFromGallery()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    public void setDefault()
    {
        imageViewProfile.setImageResource(R.drawable.default_user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String actualImage = "";
        try
        {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
            {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null)
                {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    actualImage = cursor.getString(columnIndex);
                    cursor.close();
                }

                imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(actualImage));

                Bitmap bm = BitmapFactory.decodeFile(actualImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                if (CommonUtility.isNetworkAvailable(mContext))
                {
//                    showLoadingDialog();
                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put(getString(R.string.profile_image), encodedImage.toString() == null ? "" : encodedImage.toString());
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(mContext, R.string.error_internet_not_connected, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(mContext, R.string.error_you_have_not_picked_image, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {

        switch (label) {
        }

    }


    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
        }
    }
}
