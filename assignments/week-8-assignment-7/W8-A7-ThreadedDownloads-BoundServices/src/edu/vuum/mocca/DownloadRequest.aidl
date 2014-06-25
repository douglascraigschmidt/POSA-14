package edu.vuum.mocca;

import edu.vuum.mocca.DownloadCallback;

/**
 * @class DownloadRequest
 *
 * @brief An AIDL interface for downloading an image from another
 *        process. The caller should provide a DownloadCallback object
 *        so that the downloading process can return a result across
 *        process boundaries asynchronously.
 *
 *		This file generates a java interface in /gen
 */
interface DownloadRequest {
    // Download an image at the given uri and store it on the file system.
    // When finished, call the appropriate method on the callback object to
    // send the downloaded file's file name on the file system.
    oneway void downloadImage (in Uri uri, 
                               in DownloadCallback callback); 
}
