/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;

/**
 *
 * @author Biplav
 */
public class Self extends Person {

    PrivateKey privKey;
    public static Self SELF = new Self();

    private Self() {
        privKey = null;
    }

    public static Self getSelf() {
        if ("".equals(SELF.name)) {
            SELF = loadFromFile();
            if ("".equals(SELF.name)) {                
                System.out.println("Enter your Name : ");
                Scanner input = new Scanner(System.in);
                String name = input.nextLine();
                SELF.name = name;
                SELF.macAddress = name;
                SELF.generateKeys();
                SELF.save();
            }
        }
        return SELF;
    }
//    public Self(String name, String macAddress) {
//        super(name, macAddress);
//    }
//
//    public Self(String name, String macAddress, PublicKey pubKey, PrivateKey privKey) {
//        super(name, macAddress, pubKey);
//        this.privKey = privKey;
//    }

    public void generateKeys() {
        BigInteger p = CryptoService.generatePrime(130);
        BigInteger q = CryptoService.generatePrime(130);
        BigInteger e = BigInteger.ZERO;
        BigInteger d = BigInteger.ZERO;
        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        BigInteger qMinus1 = q.subtract(BigInteger.ONE);
        BigInteger phi = pMinus1.multiply(qMinus1);
        BigInteger sum;
        boolean found = false;
        for (BigInteger ee = BigInteger.ONE; ee.compareTo(phi) < 0;) {
            ee = ee.add(BigInteger.valueOf(2));
            if (pMinus1.remainder(ee).equals(BigInteger.ZERO)
                    || qMinus1.remainder(ee).equals(BigInteger.ZERO)) {
                continue;
            }
            sum = BigInteger.ONE;
            for (BigInteger k = BigInteger.ZERO; k.compareTo(phi) < 0;) {
                k = k.add(BigInteger.ONE);
                if (k.remainder(ee).equals(BigInteger.ZERO)) {
                    continue;
                }
                sum = sum.add(phi);
                if (sum.remainder(ee).equals(BigInteger.ZERO)) {
                    e = ee;
                    d = sum.divide(e);
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        pubKey = new PublicKey(e.toString(), p.multiply(q).toString());
        privKey = new PrivateKey(d.toString(), p.toString(), q.toString(), p.multiply(q).toString());
    }

    @Override
    public String decryptMessage(String encryptedMessage) {
        StringBuilder msg = new StringBuilder();
        StringBuilder token = new StringBuilder();
        char prevChar = ' ';
        char thisChar;
        for (int i = 0; i < encryptedMessage.length(); i++) {
            thisChar = encryptedMessage.charAt(i);
            if (thisChar == ' ') {
                if (prevChar == ' ') {
                    msg.append(' ');
                } else {
                    msg.append((char) Integer.parseInt(CryptoService.encrypt(Integer.parseInt(token.toString()), (Key) privKey)));
                    token.delete(0, token.length());
                }
            } else {
                token.append(thisChar);
            }
            prevChar = thisChar;
        }
        return msg.toString();
    }

    public JsonObject toJson() {
        JsonObject myJson = toJsonNormal();
        myJson.addProperty("imagePath", imagePath);
        JsonObject privKeyJson = new JsonObject();
        privKeyJson.addProperty("d", privKey.getE());
        privKeyJson.addProperty("prime1", privKey.firstPrime);
        privKeyJson.addProperty("prime2", privKey.secondPrime);
        myJson.add("privateKey", privKeyJson);

        return myJson;
    }

    public void save() {
        try (PrintWriter out = new PrintWriter("self.lm")) {
            out.print(toJson().toString());
        } catch (IOException ex) {

        }
    }

    public static Self loadFromFile() {
        String line;
        try {
            FileReader fr = new FileReader("self.lm");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            JsonObject myJson = new JsonParser().parse(line).getAsJsonObject();
            SELF.name = myJson.get("name").getAsString();
            SELF.macAddress = myJson.get("macAddress").getAsString();
            SELF.pubKey = new PublicKey(myJson.get("publicKey").getAsJsonObject());
            SELF.privKey = new PrivateKey(myJson.get("privateKey").getAsJsonObject(), SELF.pubKey.getProduct());
            SELF.imagePath = myJson.get("imagePath").getAsString();

        } catch (IOException ie) {

        }

        return SELF;
    }

    public void display() {
        System.out.println("Name -> " + name);
        System.out.println("Mac Address -> " + macAddress);
        System.out.println("Public Key - e -> " + pubKey.getE());
        System.out.println("Public Key - product -> " + pubKey.getProduct());
        System.out.println("Private Key - d -> " + privKey.getE());
        System.out.println("Private Key - product -> " + privKey.getProduct());
        System.out.println("Prime 1 -> " + privKey.firstPrime);
        System.out.println("Prime 2 -> " + privKey.secondPrime);
    }
}
