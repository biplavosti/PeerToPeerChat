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
public class PrivateKey implements Key {

    String decryptionKey;
    String firstPrime;
    String secondPrime;
    String primeProduct;

    public PrivateKey(String d, String prime1, String prime2, String product) {
        decryptionKey = d;
        firstPrime = prime1;
        secondPrime = prime2;
        primeProduct = product;
    }

    public PrivateKey(JsonObject pk, String product) {
        this(pk.get("d").getAsString(), pk.get("prime1").getAsString(), pk.get("prime2").getAsString(), product);
    }

    @Override
    public String getE() {
        return decryptionKey;
    }

    @Override
    public String getProduct() {
        return primeProduct;
    }
}
