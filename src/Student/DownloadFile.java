/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Hamid
 */
public class DownloadFile {

    public static boolean downloadFile(String fileURL, String saveDir)
            throws IOException {

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        boolean result = false;

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            //String disposition = httpConn.getHeaderField("Content-Disposition");
            //String contentType = httpConn.getContentType();
            //int contentLength = httpConn.getContentLength();

            /*if (disposition != null) {
             // extracts file name from header field
             int index = disposition.indexOf("filename=");
             if (index > 0) {
             fileName = disposition.substring(index + 10,
             disposition.length() - 1);
             }
             } else {*/
            // extracts file name from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                    fileURL.length());
            //}

            //System.out.println("Content-Type = " + contentType);
            // System.out.println("Content-Disposition = " + disposition);
            //System.out.println("Content-Length = " + contentLength);
            // System.out.println("fileName = " + fileName);
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
            //String saveFilePath = saveDir + + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            result = true;
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return result;
    }
}
