/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Biplav
 */
public class UDPClient {

    private final DatagramSocket socket;

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();        
    }

    public void sendMessage(String message, String host, int port) throws IOException {
        System.out.println("message : " + message);
        byte buf[] = message.getBytes();
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
    
    public void broadCast(String message) throws IOException{
        socket.setBroadcast(true);
        sendMessage(message,"255.255.255.255", 29236);
    }

}
