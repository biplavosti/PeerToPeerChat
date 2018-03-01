/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Biplav
 */
public class TCPServer implements Runnable {

    private final ServerSocket server;

    public TCPServer(ServerSocket server) {
        this.server = server;
    }

    public void close() throws IOException {
        server.close();
    }

    private class ClientHandler implements Runnable {

        Socket sock;

        private ClientHandler(Socket socket) {
            try {
                sock = socket;
                sock.setSoTimeout(10000);

            } catch (IOException ex) {
            }
        }

        @Override
        public void run() {
            ObjectOutputStream outStream = null;
            ObjectInputStream inStream;
            try {
                outStream = new ObjectOutputStream(sock.getOutputStream());
                inStream = new ObjectInputStream(sock.getInputStream());

                Object object = inStream.readObject();
                System.out.println("totoototo message arrived");
                String peerIPAddress = sock.getInetAddress().getHostAddress();
                System.out.println("peer msg ip : " + peerIPAddress);
                HashMap<String, String> map = PeerToPeerChat.PEERIPMAP;
                if (object instanceof String) {
                    System.out.println("message : " + (String) object);
                    JsonObject jsonObject = new JsonParser().parse((String) object).getAsJsonObject();

                    String receiverMac = jsonObject.get("receiverMac").getAsString();
                    if (!receiverMac.equals(Self.getSelf().getMacAddress())) {
                        System.out.println("not me message");
                        outStream.writeObject("not me");
                        return;
                    }

                    String message = jsonObject.get("content").getAsString();
                    String timeStamp = jsonObject.get("timeStamp").getAsString();
                    String peerMac = jsonObject.get("senderMac").getAsString();
                    Peer correspondingPeer = null;
                    if (!map.get(peerMac).equals(peerIPAddress)) {
                        System.out.println("no such peer");
                        throw new NullPointerException();
                    }
                    for (Person peer : PeerToPeerChat.PEERLIST) {
                        if (peer.getMacAddress().equals(peerMac)) {
                            if (peer instanceof Peer) {
                                correspondingPeer = (Peer) peer;
                            }
                            break;
                        }
                    }
                    if (correspondingPeer != null) {
                        Message msg = new Message(message, timeStamp);
                        correspondingPeer.addMessage(msg);
                        correspondingPeer.saveMessage(msg);
                    }
                }
            } catch (IOException | ClassNotFoundException | NullPointerException e) {
                if (outStream != null) {
                    try {
                        e.printStackTrace();
                        outStream.writeObject("");
                    } catch (IOException ex) {
                    }
                }
            }

        }
    }

    @Override
    public void run() {
        try {
            System.out.println("tcp server started at port: " + server.getLocalPort());
            while (PeerToPeerChat.OPEN) {
                Socket sock = server.accept();
                if (PeerToPeerChat.OPEN) {
                    new Thread(new ClientHandler(sock)).start();
                }
            }
        } catch (IOException ex) {
        }
    }
}
