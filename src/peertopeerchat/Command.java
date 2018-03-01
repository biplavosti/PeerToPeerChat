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
public class Command {

    public static JsonObject addMeConstructor(boolean isReply) {
        JsonObject addMeJson = new JsonObject();
        if (!isReply) {
            addMeJson.addProperty("cmd", "addMeAsPeer");
        } else {
            addMeJson.addProperty("cmd", "replyToAddMeAsPeer");
        }
        addMeJson.addProperty("port", 29236);
        addMeJson.add("Peer", Self.getSelf().toJsonNormal());
        return addMeJson;
    }
}
