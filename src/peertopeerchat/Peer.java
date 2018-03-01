/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Biplav
 */
public class Peer extends Person {

    String userDefinedName;

    public Peer(JsonObject peer) {
        super(peer.get("name").getAsString(),
                peer.get("macAddress").getAsString(),
                new PublicKey(peer.get("publicKey").getAsJsonObject()));

        userDefinedName = name;
        imagePath = peer.get("imagePath").getAsString();
    }

    public Peer(Peer friend) {
        super(friend.name, friend.macAddress, friend.getPubKey());
        userDefinedName = name;
        imagePath = friend.getImagePath();
    }

    public void setUserDefinedName(String userDefinedName) {
        this.userDefinedName = userDefinedName;
    }

    public String getUserDefinedName() {
        return userDefinedName;
    }

    public JsonObject toJson() {
        JsonObject myJson = toJsonNormal();
        myJson.addProperty("imagepath", imagePath);
        myJson.addProperty("userDefinedName", userDefinedName);

        return myJson;
    }

    public void save() {
        try (FileWriter fw = new FileWriter("peer.lm", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(toJson().toString());
        } catch (IOException e) {
            // File writing/opening failed at some stage.
        }
    }

    @Override
    public String decryptMessage(String encryptedMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
