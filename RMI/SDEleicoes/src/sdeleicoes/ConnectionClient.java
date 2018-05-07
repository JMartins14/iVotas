/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.rmi.Remote;
import java.util.concurrent.CopyOnWriteArrayList;

public interface ConnectionClient extends Remote{
    public boolean print_on_server() throws java.rmi.RemoteException;
    public CopyOnWriteArrayList<String> clients() throws java.rmi.RemoteException;
}
