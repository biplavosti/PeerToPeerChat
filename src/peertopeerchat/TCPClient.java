/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Biplav
 */
public class TCPClient {

    public void sendMessage(Peer peer, Message message) {

    }

    public String send(String host, String message) {
        String result = "";
        try {
            Socket client = new Socket(host, 29236);
            client.setSoTimeout(5000);
            ObjectOutputStream outStream = new ObjectOutputStream(client.getOutputStream());
            outStream.writeObject(message);
            outStream.flush();
            ObjectInputStream inStream = new ObjectInputStream(client.getInputStream());
            result = (String) inStream.readObject();
//            outStream.writeObject(message);

        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
