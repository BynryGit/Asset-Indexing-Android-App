package com.aia.imgutility;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.widget.ImageView;

import com.aia.utility.AppConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * class contains methods related to getting image from server and string64
 * conversion of image
 */
public class ProcessImage {

    // string64 conversion of image
    public static String getEncodedBitmap(ImageView pImageView) {

        Bitmap bMap = ((BitmapDrawable) pImageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedString;
    }

    // convert bitmap to uri
    public static Uri getImageUri(Context pContext, Bitmap pImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        pImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = Images.Media.insertImage(pContext.getContentResolver(),
                pImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     *
     * @param pImageurl
     * @param pImageView
     * @param pProgressBar
     * @param pContext
     * @param pType
     */
    // Use universal image loader to get image from server
    /*public static void getImage(String pImageurl, ImageView pImageView,
			final ProgressBar pProgressBar, final Context pContext,
			final int pType, final boolean pIsDownloaded) {

		ShareZ.getImageLoader(pContext).displayImage(pImageurl, pImageView,
				new ImageLoadingListener() {
					boolean lCacheFound;

					@Override
					public void onLoadingStarted(String pImageUri, View pView) {
						if (pProgressBar != null)
							pProgressBar.setVisibility(ProgressBar.VISIBLE);

						List<String> lMemCache = MemoryCacheUtils
								.findCacheKeysForImageUri(pImageUri,
										ImageLoader.getInstance()
												.getMemoryCache());
						lCacheFound = !lMemCache.isEmpty();
						if (!lCacheFound) {

							File lDiscCache = DiskCacheUtils.findInCache(
									pImageUri, ImageLoader.getInstance()
											.getDiskCache());
							if (lDiscCache != null) {
								lCacheFound = lDiscCache.exists();
							}
						}
					}

					@Override
					public void onLoadingFailed(String pImageUri, View pView,
							FailReason pFailReason) {

						if (pType == Constants.TYPE_FILE) {
							if (pImageUri == null)
								return;

							// applies to filelistadapter only
							if (pView instanceof ImageView) {
								ImageView lImageView = (ImageView) pView;
								String lFilePath;
								if (pImageUri
										.equals(Constants.FILE_TYPE_FOLDER)) {
									lFilePath = Constants.FILE_TYPE_FOLDER;
								} else {
									lFilePath = FileOperations.getFilePath(
											pContext, Uri.parse(pImageUri));
								}
								lImageView
										.setImageBitmap(getBitmap(lFilePath,
												pContext, lImageView, 1,
												pIsDownloaded));
							}
						} else if (pType == Constants.TYPE_CONTACT) {

							if (pView instanceof ImageView) {

								ImageView lImageView = (ImageView) pView;
								lImageView.setImageDrawable(pContext
										.getResources().getDrawable(
												R.drawable.ic_avatar));
							}
						}

						if (pProgressBar != null)
							pProgressBar.setVisibility(ProgressBar.GONE);

					}

					@Override
					public void onLoadingComplete(String pImageUri, View pView,
							Bitmap pLoadedImage) {

						if (pProgressBar != null)
							pProgressBar.setVisibility(ProgressBar.GONE);

						if (pType == Constants.TYPE_FILE) {
							if (pImageUri == null)
								return;

							// applies to filelistadapter only
							if (pView instanceof ImageView) {
								ImageView lImageView = (ImageView) pView;
								String lFilePath;
								if (pImageUri
										.equals(Constants.FILE_TYPE_FOLDER)) {
									lFilePath = Constants.FILE_TYPE_FOLDER;
								} else {
									lFilePath = FileOperations.getFilePath(
											pContext, Uri.parse(pImageUri));
								}
								lImageView
										.setImageBitmap(getBitmap(lFilePath,
												pContext, lImageView, 1,
												pIsDownloaded));
							}
						}
					}

					@Override
					public void onLoadingCancelled(String pImageUri, View pView) {
						if (pProgressBar != null)
							pProgressBar.setVisibility(ProgressBar.GONE);

					}
				});

	}*/

    /**
     *
     * @param pFilePath
     * @param pContext
     * @param pImageView
     * @param pScaleType
     * @return
     */
	/*public static Bitmap getBitmap(String pFilePath, Context pContext,
			ImageView pImageView, int pScaleType, boolean pIsDownloaded) {

		Drawable lDrawable = pContext.getResources().getDrawable(
				FileOperations.getIcon(pFilePath));

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = getSampleSize(pFilePath,
				lDrawable.getIntrinsicHeight() * pScaleType,
				lDrawable.getIntrinsicWidth() * pScaleType);

		options.inJustDecodeBounds = false;
		Bitmap lBitmap = null;
		if (options.inSampleSize != 0)
			lBitmap = BitmapFactory.decodeFile(pFilePath, options);

		if (lBitmap == null) {
			if (pIsDownloaded)
				lBitmap = ThumbnailUtils.createVideoThumbnail(pFilePath,
						Thumbnails.MICRO_KIND);

			if (lBitmap != null)
				return lBitmap;

			Log.d("ImageLoadTask", "lBitmap is null ....");
			return BitmapFactory.decodeResource(pContext.getResources(),
					FileOperations.getIcon(pFilePath));
		}

		setImageBound(pImageView, pScaleType, lDrawable);

		if (pIsDownloaded) {
			lBitmap = ProcessImage.rotateBitmap(lBitmap,
					getImageContentUri(pContext, new File(pFilePath))
							.toString(), pContext);
		} else {
			lBitmap = BitmapFactory.decodeResource(pContext.getResources(),
					FileOperations.getIcon(pFilePath));
		}

		// }
		return lBitmap;
	}*/

    /**
     * @param pImageView
     * @param pScaleType
     * @param pDrawable
     */
    private static void setImageBound(ImageView pImageView, int pScaleType,
                                      Drawable pDrawable) {
        ImageView lImageView = pImageView;// mImageViewReference.get();
        lImageView.getLayoutParams().height = pDrawable.getIntrinsicHeight()
                * pScaleType;
        lImageView.getLayoutParams().width = pDrawable.getIntrinsicWidth()
                * pScaleType;
    }

    /**
     * @param pPath
     * @param pReqHeight
     * @param pReqWidth
     * @return
     */
    public static int getSampleSize(String pPath, int pReqHeight, int pReqWidth) {
        if (!isPathExist(pPath))
            return 0;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > pReqHeight || width > pReqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > pReqHeight
                    && (halfWidth / inSampleSize) > pReqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * @param pPath
     * @return
     */
    private static boolean isPathExist(String pPath) {
        if (pPath == null)
            return false;
        return new File(pPath).exists();
    }

    /**
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{Images.Media._ID},
                Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * @return
     */
    public static Uri getOutputUri(boolean pIsVideo) {
        File lFile = Environment.getExternalStorageDirectory();
        if (lFile == null) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return null;
            }

        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());

        String lPath;

        String lFileName;

        if (pIsVideo) {
            lPath = AppConstants.VIDEO_PATH;
            lFileName = "VID_" + timeStamp + ".mp4";
        } else {
            lPath = AppConstants.IMAGE_PATH;
            lFileName = "IMG_" + timeStamp + ".jpg";
        }
        lFile = new File(Environment.getExternalStorageDirectory() + lPath);

        if (!lFile.exists()) {
            lFile.mkdirs();
        }

        // Create a media file name

        lFile = new File(Environment.getExternalStorageDirectory() + lPath
                + "/" + lFileName);

        //	Log.v("ProcessImage", "" + lFile.getAbsolutePath());

        return Uri.fromFile(lFile);
    }

    /**
     */
    public static String removeUriFromPath(String pUri) {
        return pUri.substring(7, pUri.length());
    }

    // /**
    // *
    // * @param bitmap
    // * @param orientation
    // * @return
    // */
    // public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
    //
    // Matrix matrix = new Matrix();
    // switch (orientation) {
    // case ExifInterface.ORIENTATION_NORMAL:
    // return bitmap;
    // case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
    // matrix.setScale(-1, 1);
    // break;
    // case ExifInterface.ORIENTATION_ROTATE_180:
    // matrix.setRotate(180);
    // break;
    // case ExifInterface.ORIENTATION_FLIP_VERTICAL:
    // matrix.setRotate(180);
    // matrix.postScale(-1, 1);
    // break;
    // case ExifInterface.ORIENTATION_TRANSPOSE:
    // matrix.setRotate(90);
    // matrix.postScale(-1, 1);
    // break;
    // case ExifInterface.ORIENTATION_ROTATE_90:
    // matrix.setRotate(90);
    // break;
    // case ExifInterface.ORIENTATION_TRANSVERSE:
    // matrix.setRotate(-90);
    // matrix.postScale(-1, 1);
    // break;
    // case ExifInterface.ORIENTATION_ROTATE_270:
    // matrix.setRotate(-90);
    // break;
    // default:
    // return bitmap;
    // }
    // try {
    // Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
    // bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    // bitmap.recycle();
    // return bmRotated;
    // } catch (OutOfMemoryError e) {
    // e.printStackTrace();
    // return null;
    // }
    //
    // }

    /**
     * @param pBitmap
     * @param //orientation
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap pBitmap, String pFilepath,
                                      Context pContext) {
        String lPicturePath;
        try {

            if (pFilepath.startsWith("file")) {
                lPicturePath = pFilepath;
            } else {
                String[] lFilePathColumn = {Images.Media.DATA};

                Cursor lCursor = pContext.getContentResolver()
                        .query(Uri.parse(pFilepath), lFilePathColumn, null,
                                null, null);
                lCursor.moveToFirst();

                int lColumnIndex = lCursor.getColumnIndex(lFilePathColumn[0]);
                lPicturePath = lCursor.getString(lColumnIndex);
            }
            ExifInterface lExif = new ExifInterface(lPicturePath);
            int exifOrientation = lExif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            // Log.d(TAG,
            // "Rotation bitmap.getWidth = "+bitmap.getWidth()+" bitmap.getHeight = "+bitmap.getHeight());
            //
            // rotate = bitmap.getWidth() > bitmap.getHeight() ? 0 : 90;

            if (rotate != 0) {

                int w = pBitmap.getWidth();
                int h = pBitmap.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                pBitmap = Bitmap.createBitmap(pBitmap, 0, 0, w, h, mtx, false);
            }

            pBitmap = pBitmap.copy(Bitmap.Config.ARGB_8888, true);

            return pBitmap;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception lE) {
            lE.printStackTrace();
        }

        return pBitmap;

    }

    /**
     * This function is used to show profile picture in full screen.
     *
     * @param pActivity
     * @param pImageUrl
     */
	/*public static void showProfleImageDialog(final Activity pActivity,
			String pImageUrl) {

		AlertDialog.Builder lBuilder = new AlertDialog.Builder(pActivity);
		LayoutInflater lInflater = LayoutInflater.from(pActivity);
		View lView = lInflater.inflate(R.layout.user_profile_pic_dialog, null);
		lBuilder.setView(lView);
		lBuilder.setCancelable(true);

		final ImageView mImageViewProfileDialog = (ImageView) lView
				.findViewById(R.id.imgUserProfile_UserProfilePicDialog);
		ProcessImage.getImage(pImageUrl, mImageViewProfileDialog, null,
				pActivity, Constants.TYPE_CONTACT, true);

		AlertDialog lAlertDialog = lBuilder.create();
		lAlertDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		lAlertDialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
				LayoutParams.FLAG_FULLSCREEN);
		lAlertDialog.show();
	}*/

}