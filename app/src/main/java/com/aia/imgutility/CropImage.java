package com.aia.imgutility;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.aia.R;
import com.aia.utility.AppConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;


/**
 * The activity can crop specific region of interest from an image.
 */
@SuppressLint("NewApi")
public class CropImage extends MonitoredActivity {
    public static final int NO_STORAGE_ERROR = -1;

    // These are various options can be specified in the intent.
    public static final int CANNOT_STAT_ERROR = -2;
    private static final String TAG = "CropImage";
    private final Handler mHandler = new Handler();
    private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
    boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    boolean mSaving = false; // Whether the "save" button is already clicked.
    HighlightView mCrop;
    private Uri mSaveUri;
    private int mAspectX, mAspectY;
    private boolean mCircleCrop = false;
    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mOutputX, mOutputY;
    private boolean mScale;
    private boolean mScaleUp = true;
    private boolean mDoFaceDetection = true;
    private CropImageView mImageView;
    private ContentResolver mContentResolver;
    private Bitmap mBitmap;
    Runnable mRunFaceDetection = new Runnable() {

        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right
                        - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                        faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mImageView.mHighlightViews.clear(); // Thong added for rotate

            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
                        faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        Toast t = Toast.makeText(CropImage.this,
                                "Multi face crop help", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            });
        }
    };
    private IImage mImage;
    private String mImagePath;

    private static Uri getImageUri(String path) {

        try {
            return Uri.parse(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        if (photoUri == null)
            return -1;

        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        if (cursor == null)
            return -1;

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContentResolver = getContentResolver();

        setContentView(R.layout.cropimage);

        mImageView = (CropImageView) findViewById(R.id.image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

		/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);*/

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getString("circleCrop") != null) {
                mCircleCrop = true;
                mAspectX = 1;
                mAspectY = 1;
            }

            mImagePath = extras.getString("image-path");
            //	Log.v(TAG, "" + mImagePath);
            if (mImagePath == null) {
                Toast.makeText(CropImage.this,
                        "Image not found in local storage", Toast.LENGTH_SHORT)
                     .show();
                finish();
                return;
            }

            mSaveUri = getImageUri(mImagePath);

            if (mSaveUri == null) {
                Toast.makeText(CropImage.this,
                        "Image not found in local storage", Toast.LENGTH_SHORT)
                     .show();
                finish();
                return;
            }

            if (extras.getString(MediaStore.EXTRA_OUTPUT) != null) {
                mSaveUri = getImageUri(extras
                        .getString(MediaStore.EXTRA_OUTPUT));
            }

            mBitmap = getBitmap(mImagePath);

            mAspectX = extras.getInt("aspectX");
            mAspectY = extras.getInt("aspectY");
            mOutputX = extras.getInt("outputX");
            mOutputY = extras.getInt("outputY");
            mScale = extras.getBoolean("scale", true);
            mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);

        }

        if (mBitmap == null) {
            Toast.makeText(CropImage.this, "Image not found in local storage",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.discard).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        startFaceDetection();

    }

    private Bitmap getBitmap(String path) {
        Uri uri = getImageUri(path);
        // Log.v("oreintation", "" + uri);
        InputStream lIn = null;
        // int lOrientation = getOrientation(CropImage.this, uri);
        // if (lOrientation == -1) {
        // try {
        // ExifInterface lExif;
        //
        // lExif = new ExifInterface(ProcessImage.removeUriFromPath(path));
        //
        // lOrientation = lExif.getAttributeInt(
        // ExifInterface.TAG_ORIENTATION,
        // ExifInterface.ORIENTATION_UNDEFINED);
        // } catch (IOException e) {
        //
        // e.printStackTrace();
        // }
        // }
        //
        // Log.v("oreintation", "" + lOrientation);
        try {
            // final int IMAGE_MAX_SIZE = 2048;
            final int IMAGE_MAX_SIZE = 2048;
            lIn = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(lIn, null, o);
            lIn.close();

            int lScale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                lScale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            BitmapFactory.Options lO2 = new BitmapFactory.Options();
            lO2.inSampleSize = lScale;
            lIn = mContentResolver.openInputStream(uri);
            Bitmap lBit = BitmapFactory.decodeStream(lIn, null, lO2);
            lIn.close();

            lBit = ProcessImage.rotateBitmap(lBit, path, CropImage.this);
            // if (orientation > 0) {
            // Matrix matrix = new Matrix();
            // matrix.postRotate(orientation);
            //
            // b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
            // matrix, true);
            // } else if (orientation == -1) {
            // b=ProcessImage.rotateBitmap(b, orientation)
            // // Matrix matrix = new Matrix();
            // // matrix.preRotate(90);
            // //
            // // b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
            // // matrix, true);
            // }

            return lBit;
        } catch (FileNotFoundException e) {
            //	Log.e(TAG, "file " + path + " not found");
        } catch (IOException e) {
            //	Log.e(TAG, "file " + path + " not found");
        } catch (Exception e) {
            //	Log.e(TAG, "file " + path + " not found");
        }
        return null;
    }

    private void startFaceDetection() {
        if (isFinishing()) {
            return;
        }

        mImageView.setImageBitmapResetBase(mBitmap, true);
        mImageView.setScaleType(ScaleType.FIT_CENTER);

        com.aia.imgutility.Util.startBackgroundJob(this, null, "Please wait\u2026",
                new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        final Bitmap b = (mImage != null) ? mImage
                                .fullSizeBitmap(IImage.UNCONSTRAINED,
                                        1024 * 1024) : mBitmap;

                        mHandler.post(new Runnable() {
                            public void run() {
                                if (b != mBitmap && b != null) {
                                    mImageView.setImageBitmapResetBase(b, true);
                                    mImageView
                                            .setScaleType(ScaleType.FIT_CENTER);
                                    mBitmap.recycle();
                                    mBitmap = b;
                                }
                                if (mImageView.getScale() == 1F) {
                                    mImageView
                                            .setScaleType(ScaleType.FIT_CENTER);
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        mRunFaceDetection.run();
                    }
                }, mHandler);
    }

    private void onSaveClicked() {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return;

        if (mCrop == null) {
            return;
        }

        mSaving = true;

        Rect r = mCrop.getCropRect();

        int width = r.width();
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        Bitmap croppedImage = Bitmap.createBitmap(width, height,
                mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(mBitmap, r, dstRect, null);
        }

        if (mCircleCrop) {
            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle. So we fill in the
            // area around the circle with alpha. Note the all important
            // PortDuff.Mode.CLEAR.
            Canvas c = new Canvas(croppedImage);
            Path p = new Path();
            p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
            c.clipPath(p, Region.Op.DIFFERENCE);
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        }

		/* If the output is required to a specific size then scale or fill */
        if (mOutputX != 0 && mOutputY != 0) {
            if (mScale) {
				/* Scale the image to the required dimensions */
                Bitmap old = croppedImage;
                croppedImage = com.aia.imgutility.Util.transform(new Matrix(), croppedImage,
                        mOutputX, mOutputY, mScaleUp);
                if (old != croppedImage) {
                    old.recycle();
                }
            } else {

				/*
				 * Don't scale the image crop it to the size requested. Create
				 * an new image with the cropped image in the center and the
				 * extra space filled.
				 */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
                        Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(b);

                Rect srcRect = mCrop.getCropRect();
                Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                int dx = (srcRect.width() - dstRect.width()) / 2;
                int dy = (srcRect.height() - dstRect.height()) / 2;

				/* If the srcRect is too big, use the center part of it. */
                srcRect.inset(Math.max(0, dx), Math.max(0, dy));

				/* If the dstRect is too big, use the center part of it. */
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

				/* Draw the cropped bitmap in the center */
                canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

				/* Set the cropped bitmap as the new bitmap */
                croppedImage.recycle();
                croppedImage = b;
            }
        }

        final Bitmap b = croppedImage;
        com.aia.imgutility.Util.startBackgroundJob(this, null, "Saving image", new Runnable() {
            public void run() {
                saveOutput(b);
            }
        }, mHandler);

    }

    /**
     * @param croppedImage
     */
    private void saveOutput(Bitmap croppedImage) {

        String lSaveUri = MediaStore.Images.Media.insertImage(
                getContentResolver(), croppedImage, "Titlec", "");

        Intent lIntent = new Intent();
        lIntent.putExtra(AppConstants.CROPPED_IMAGE, lSaveUri);
        setResult(RESULT_OK, lIntent);
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null && mBitmap.isRecycled())
            mBitmap.recycle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem) {

        switch (pItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}
