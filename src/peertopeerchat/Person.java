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
import java.util.LinkedList;

/**
 *
 * @author Biplav
 */
public abstract class Person {

    String name;
    String macAddress;
    PublicKey pubKey;
    String imagePath;

    LinkedList<Message> messages = new LinkedList();

    public Person() {
        name = "";
        macAddress = "";
        pubKey = null;
        imagePath = "";
    }

    public Person(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public Person(String name, String macAddress, PublicKey pubKey) {
        this(name, macAddress);
        this.pubKey = pubKey;
        imagePath = "";
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setPubKey(PublicKey pubKey) {
        this.pubKey = pubKey;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public JsonObject toJsonNormal() {
        JsonObject myJson = new JsonObject();
        myJson.addProperty("name", name);
        myJson.addProperty("macAddress", macAddress);
        JsonObject publicKeyJson = new JsonObject();
        publicKeyJson.addProperty("e", pubKey.encryptionKey);
        publicKeyJson.addProperty("product", pubKey.primeProduct);
        myJson.add("publicKey", publicKeyJson);
        myJson.addProperty("imagePath", imagePath);

        return myJson;
    }

    public void addMessage(Message msg) {
        messages.add(msg);
    }

    public String encryptMessage(String orig) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < orig.length(); i++) {
            char c = orig.charAt(i);
            if (c != ' ') {
                str.append(CryptoService.encrypt((int) c, (Key) pubKey));
            }
            str.append(' ');
        }
        return str.toString();
    }
    
    public abstract String decryptMessage(String encryptedMessage);

    public void saveMessage(Message msg) {
        String fileName = macAddress + ".lm";
        try (FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(msg.toJson().toString());
        } catch (IOException e) {
            // File writing/opening failed at some stage.
        }
    }

    public void dumpMessage() {
        if (!messages.isEmpty()) {
            String fileName = macAddress + ".lm";
            try (PrintWriter out = new PrintWriter(fileName)) {
                for (Message msg : messages) {
                    out.println(msg.toJson().toString());
                }
            } catch (IOException ex) {

            }
        }
    }

}
