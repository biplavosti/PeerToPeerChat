/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

/**
 *
 * @author Biplav
 */
public class Chat {

    Peer peer;
    Message message;

    public Chat(Peer friend) {
        peer = friend;
        message = null;
    }

    public void setMessage(Message msg) {
        message = msg;
    }

    public void send() {
        new Thread() {
            @Override
            public void run() {
                new TCPClient().sendMessage(peer, message);
            }
        }.start();
    }
}
