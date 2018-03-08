/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hamid
 */
public class OpenFile {
    
    public static void openFile(String fileUrl){
    try {
         File myFile = new File(fileUrl);
         Desktop.getDesktop().open(myFile);
         } catch (IOException ex) {
         // no application registered for PDFs
         System.out.println("ERRROR In uploading File"+ex.getMessage());
         }
    }
}
