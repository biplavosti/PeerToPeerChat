/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Biplav
 */
public class PeerToPeerChat {

    public static volatile boolean OPEN;
    public static ArrayList<Person> PEERLIST = new ArrayList();
    public static HashMap<String, String> PEERIPMAP = new HashMap();
    public static TCPServer server;
    public static UDPServer udpServer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Self myself = Self.getSelf();
        myself.display();
        PEERLIST.add(myself);
        OPEN = true;

        try {
            udpServer = new UDPServer(29236);
            new Thread(udpServer).start();
            server = new TCPServer(new ServerSocket(29236));
            new Thread(server).start();
            new UDPClient().broadCast(Command.addMeConstructor(false).toString());

            firstWindow();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void firstWindow() {
        new Thread() {
            @Override
            public void run() {
                int index;
                Scanner input = new Scanner(System.in);
                while (true) {
                    System.out.println("-------------------");
                    index = 1;
                    for (String key : PEERIPMAP.keySet()) {
                        System.out.println("mac : " + key + " ip : " + PEERIPMAP.get(key));
                    }
                    System.out.println();
                    for (Person peer : PEERLIST) {
                        System.out.println(index + " : " + peer.getName());
                        index++;
                    }

                    System.out.println("Enter the friend number to chat with that friend");
                    System.out.println("Or Enter Zero (0) to exit");
                    System.out.println("Or Enter -1 (minus 1) to refresh friend list");
                    int choice = input.nextInt();
                    if (choice == 0) {
                        try {
                            OPEN = false;
                            server.close();
                            udpServer.close();
                        } catch (IOException ex) {
                        }
                        break;
                    } else if (choice == -1) {
                        continue;
                    }
                    showPeerChatScreen(choice - 1);
                }
            }
        }.start();

    }

    public static void showPeerChatScreen(int index) {
        Scanner input = new Scanner(System.in);
        while (true) {
            Person person = PEERLIST.get(index);
            System.out.println("Host : " + PEERIPMAP.get(person.getMacAddress()));
            System.out.println("Name : " + person.getName());
            System.out.println("MAC Address : " + person.getMacAddress());
            System.out.println("Public key : ");
            person.getPubKey().display();
            System.out.println();

            for (Message msg : person.messages) {
                System.out.println(msg.content);
                System.out.println(msg.timeStamp);
            }
            System.out.println("\nType Here : ");
            String message = input.nextLine();

            if (message.equals("0")) {
                break;
            } else if (!message.equals("")) {
                Message msg = new Message(message, new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                msg.setReceiverMac(person.getMacAddress());
                msg.setSenderMac(Self.getSelf().getMacAddress());
//                message = person.encryptMessage(message);                                
//                System.out.println(message);
//                System.out.println(CryptoService.base64Encode(new BigInteger(CryptoService.hash(message))));
//                message = person.decryptMessage(message);
//                System.out.println(message);

                if (!(person instanceof Self)) {
                    new TCPClient().send(PEERIPMAP.get(person.getMacAddress()), msg.toJson().toString());
                }
                person.addMessage(msg);
                person.saveMessage(msg);
            }
        }

    }

}
