package com.aia.imgutility;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Parcelable;

import java.io.IOException;
import java.util.HashMap;

//
// ImageList and Image classes have one-to-one correspondence.
// The class hierarchy (* = abstract class):
//
//    IImageList
//    - BaseImageList (*)
//      - VideoList
//      - ImageList
//        - DrmImageList
//      - SingleImageList (contains UriImage)
//    - ImageListUber
//
//    IImage
//    - BaseImage (*)
//      - VideoObject
//      - Image
//        - DrmImage
//    - UriImage
//

/**
 * The interface of all image collections used in gallery.
 */
public interface IImageList extends Parcelable {
    HashMap<String, String> getBucketIds();

    void deactivate();

    /**
     * Returns the count of image objects.
     *
     * @return the number of images
     */
    int getCount();

    /**
     * @return true if the count of image objects is zero.
     */
    boolean isEmpty();

    /**
     * Returns the image at the ith position.
     *
     * @param i the position
     * @return the image at the ith position
     */
    IImage getImageAt(int i);

    /**
     * Returns the image with a particular Uri.
     *
     * @param uri
     * @return the image with a particular Uri. null if not found.
     */
    IImage getImageForUri(Uri uri);

    /**
     * @param image
     * @return true if the image was removed.
     */
    boolean removeImage(IImage image);

    /**
     * Removes the image at the ith position.
     *
     * @param i the position
     */
    boolean removeImageAt(int i);

    int getImageIndex(IImage image);

    /**
     * Generate thumbnail for the image (if it has not been generated.)
     *
     * @param index the position of the image
     */
    void checkThumbnail(int index) throws IOException;

    /**
     * Opens this list for operation.
     */
    void open(ContentResolver resolver);

    /**
     * Closes this list to release resources, no further operation is allowed.
     */
    void close();
}
