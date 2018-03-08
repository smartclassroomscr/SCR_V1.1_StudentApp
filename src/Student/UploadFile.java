/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Hamid
 */
public class UploadFile {

    public static String UploadFile(String ServerUrl, String FilePath) throws IOException {
        ///https://coderanch.com/t/227896/sending-image-server-HTTP-connection
        String result = "ERROR";
        String CrLf = "\r\n";
        String filename = FilePath.substring(FilePath.lastIndexOf("\\"));
        filename = filename.replaceAll(" ", "");

        URLConnection conn = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(ServerUrl);
            //System.out.println("url:" + url);
            conn = url.openConnection();
            conn.setDoOutput(true);

            String postData = "";

            /*InputStream imgIs = getClass().getResourceAsStream("C:"+File.separator+"xampp"+File.separator+"htdocs"+File.separator+"PhpProjects"+File.separator+"LocalServer.txt"); 
             byte []imgData = new byte[imgIs.available()]; 
             imgIs.read(imgData); */
            FileInputStream fileInputStream = new FileInputStream(new File(FilePath));
            byte[] fileData = new byte[fileInputStream.available()];
            fileInputStream.read(fileData);

            String message1 = "";
            message1 += "-----------------------------4664151417711" + CrLf;
//name=\"name of file variable that is received on server end\"; filename="name of file you want to show on server end"
            message1 += "Content-Disposition: form-data; name=\"uploadedfile\"; filename=" + filename + CrLf;
//message1 += "Content-Type: image/jpeg" + CrLf; 
            message1 += "Content-Type: \"text/plain" + CrLf;
            message1 += CrLf;

// the image is sent between the messages in the multipart message. 
            String message2 = "";
            message2 += CrLf + "-----------------------------4664151417711--" + CrLf;

            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------4664151417711");
// might not need to specify the content-length when sending chunked data. 
            conn.setRequestProperty("Content-Length", String.valueOf((message1.length() + message2.length() + fileData.length)));

            //System.out.println("open Output Stream");
            outputStream = conn.getOutputStream();

            //System.out.println(message1);
            outputStream.write(message1.getBytes());

// SEND THE File 
            int index = 0;
            int size = 1024;
            do {
                //System.out.println("write:" + index);
                if ((index + size) > fileData.length) {
                    size = fileData.length - index;
                }
                outputStream.write(fileData, index, size);
                index += size;
            } while (index < fileData.length);
            //System.out.println("written:" + index);

            //System.out.println(message2);
            outputStream.write(message2.getBytes());
            outputStream.flush();

            //System.out.println("open Input Stream");
            inputStream = conn.getInputStream();

            int len;
            byte[] data = new byte[Constants.BUFFER_SIZE];
            //do {
            //System.out.println("READ1");
            len = inputStream.read(data);
            result = new String(data, 0, len + 1).trim();

        } catch (Exception e) {
            System.out.println("ERROR in uploading File:" + e.getMessage());
            e.printStackTrace();
        } finally {
            //System.out.println("Close connection");
            try {
                outputStream.close();
                inputStream.close();
            } catch (Exception ex) {
            }
            // System.out.println("RESUTL:"+result);
            if (result.contains("FILENAME")) {
                //String separated[]=result.split("FILENAME");
                result = result.substring(result.indexOf("FILENAME") + 8, result.lastIndexOf("FILENAME"));

            }
            result = result.trim();
        }

        return result;
    }

    public static boolean UploadFileDetailsInDB(int classSessionId, String fileName, String uploadedBy) throws IOException, SQLException {
        boolean status = false;
        if (ConnectToDB.connection != null) {
            String sql = "INSERT INTO [Files] (ClassSession_Id,File_Name,Uploaded_By) VALUES (?, ?,?)";

            PreparedStatement statement = ConnectToDB.connection.prepareStatement(sql);
            statement.setInt(1, classSessionId);
            statement.setString(2, fileName.toString());
            statement.setString(3, uploadedBy);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                status = true;
            }
        } else {
            System.out.println("Connect To DB First");
        }
        return status;
    }

}
