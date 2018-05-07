/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Files {
        private ObjectOutputStream oS;
        private ObjectInputStream iS;
    
    public boolean OpenReadMode(String fileName) {
        try{
            iS = new ObjectInputStream(new FileInputStream(fileName));
            return true;
        } catch (IOException e){
               return false;
        }
    }
    public void OpenWriteMode(String fileName) throws IOException{
        oS = new ObjectOutputStream(new FileOutputStream(fileName));
    }
    public Object ReadObject() throws IOException,ClassNotFoundException{
        return iS.readObject();
    }
    public void WriteObject(Object o) throws IOException{
        oS.writeObject(o);
    }
    public void closeReadMode() throws IOException{
        iS.close();
    }
    public void closeWriteMode() throws IOException{
        oS.close();
    }    
}
