/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Biplav
 */
public class UDPServer implements Runnable {

    private final DatagramSocket socket;

    public UDPServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println("udp server started");
    }

    public void close() {
        socket.close();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        while (!socket.isClosed()) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);                  
                String content = new String(packet.getData(), 0, packet.getLength());
                String peerIPAddress = packet.getAddress().getHostAddress();
                System.out.println("content : " + content);
                System.out.println("address : " + peerIPAddress);

                JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
                if (jsonObject.has("cmd")) {
                    String command = jsonObject.get("cmd").getAsString();
                    if ("addMeAsPeer".equals(command) || "replyToAddMeAsPeer".equals(command)) {
                        String peerMacAddress;
                        int peerServerPort = jsonObject.get("port").getAsInt();
                        JsonObject peerJson = jsonObject.get("Peer").getAsJsonObject();
                        peerMacAddress = peerJson.get("macAddress").getAsString();
                        boolean found = false;
                        PeerToPeerChat.PEERIPMAP.put(peerMacAddress, peerIPAddress);

                        for (Person peer : PeerToPeerChat.PEERLIST) {
                            if (peer.getMacAddress().equals(peerMacAddress)) {
                                peer.setName(peerJson.get("name").getAsString());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            Peer newPeer = new Peer(peerJson);
                            PeerToPeerChat.PEERLIST.add(newPeer);
                        }
                        if ("addMeAsPeer".equals(command)) {
                            new UDPClient().sendMessage(Command.addMeConstructor(true).toString(), peerIPAddress, peerServerPort);
                        }
                    }

                }
            } catch (IOException ex) {

            }
        }
        System.out.println("UDP Server closed");
    }
}
