package edu.vuum.mocca;

/**
 * @class DownloadCall
 *
 * @brief An AIDL interface used to download an image in another
 *        process. Any invocations of downloadImage() will block until
 *        the function completes and returns from the other process.
 *
 *		This file generates a java interface in /gen
 */
interface DownloadCall {
    // Download the image at the given web uri then return a string to its location on the file system.
    String downloadImage (in Uri uri);
}
