package com.aia.imgutility;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.InputStream;

/**
 * The interface of all images used in gallery.
 */
public interface IImage {
    int THUMBNAIL_TARGET_SIZE = 320;
    int MINI_THUMB_TARGET_SIZE = 96;
    int UNCONSTRAINED = -1;
    boolean ROTATE_AS_NEEDED = true;
    boolean NO_ROTATE = false;
    boolean USE_NATIVE = true;
    boolean NO_NATIVE = false;

    /**
     * Get the image list which contains this image.
     */
    IImageList getContainer();

    /**
     * Get the bitmap for the full size image.
     */
    Bitmap fullSizeBitmap(int minSideLength,
                          int maxNumberOfPixels);

    Bitmap fullSizeBitmap(int minSideLength,
                          int maxNumberOfPixels, boolean rotateAsNeeded);

    Bitmap fullSizeBitmap(int minSideLength,
                          int maxNumberOfPixels, boolean rotateAsNeeded, boolean useNative);

    int getDegreesRotated();

    /**
     * Get the input stream associated with a given full size image.
     */
    InputStream fullSizeImageData();

    long fullSizeImageId();

    Uri fullSizeImageUri();

    /**
     * Get the path of the (full size) image data.
     */
    String getDataPath();

    String getTitle();

    // Get/Set the title of the image
    void setTitle(String name);

    // Get metadata of the image
    long getDateTaken();

    String getMimeType();

    int getWidth();

    int getHeight();

    String getDisplayName();

    // Get property of the image
    boolean isReadonly();

    boolean isDrm();

    // Get the bitmap/uri of the medium thumbnail
    Bitmap thumbBitmap(boolean rotateAsNeeded);

    Uri thumbUri();

    // Get the bitmap of the mini thumbnail.
    Bitmap miniThumbBitmap();

    // Rotate the image
    boolean rotateImageBy(int degrees);

}
