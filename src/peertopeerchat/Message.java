/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import com.google.gson.JsonObject;

/**
 *
 * @author Biplav
 */
public class Message {

    String id;
    String type;
    String content;
    String timeStamp;
    String receiverMac;
    String senderMac;
    String signature;

    public Message(String message, String timeStamp) {
        this.content = message;
        this.timeStamp = timeStamp;
        this.id = "1";
        this.receiverMac = "";
        this.signature = "";
        this.type = "Text";
    }

    public JsonObject toJson() {
        JsonObject myJson = new JsonObject();
        myJson.addProperty("id", id);
        myJson.addProperty("type", type);
        myJson.addProperty("content", content);
        myJson.addProperty("timeStamp", timeStamp);
        myJson.addProperty("receiverMac", receiverMac);
        myJson.addProperty("senderMac", senderMac);
        myJson.addProperty("signature", signature);
        return myJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReceiverMac() {
        return receiverMac;
    }

    public void setReceiverMac(String receiverMac) {
        this.receiverMac = receiverMac;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSenderMac() {
        return senderMac;
    }

    public void setSenderMac(String senderMac) {
        this.senderMac = senderMac;
    }

}
