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
public class PublicKey implements Key {

    String encryptionKey;
    String primeProduct;

    public PublicKey(String enc, String product) {
        encryptionKey = enc;
        primeProduct = product;
    }

    public PublicKey(JsonObject pubKey) {
        this(pubKey.get("e").getAsString(), pubKey.get("product").getAsString());
    }

    public boolean equals(PublicKey pk) {
        return encryptionKey.equals(pk.encryptionKey) && primeProduct.equals(pk.primeProduct);
    }

    public boolean equals(JsonObject pk) {
        return encryptionKey.equals(pk.get("e").getAsInt()) && primeProduct.equals(pk.get("product").toString());
    }

    @Override
    public String getE() {
        return encryptionKey;
    }

    @Override
    public String getProduct() {
        return primeProduct;
    }

    public void display() {
        System.out.println("Enc = " + encryptionKey);
        System.out.println("Product = " + primeProduct);
    }
}
