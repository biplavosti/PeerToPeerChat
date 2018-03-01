package peertopeerchat;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Biplav
 */
public class CryptoService {

    public static String hash(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new BigInteger(digest.digest(key.getBytes(StandardCharsets.UTF_8))).toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return "0";
    }

    public static BigInteger generatePrime(int bit) {
        return BigInteger.probablePrime(bit, new Random());
    }
    
    private static BigInteger mod(BigInteger base, BigInteger exponent, BigInteger dividend) {
        return modulus(base, exponent, dividend, BigInteger.ONE);
    }

    private static BigInteger modulus(BigInteger base, BigInteger exp, BigInteger div, BigInteger rem) {
        if (base.equals(BigInteger.ONE) || exp.equals(BigInteger.ZERO)) {
            return rem;
        } else if (exp.equals(BigInteger.ONE)) {
            return base.multiply(rem).remainder(div);
        } else if (base.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }

        if (base.compareTo(div) >= 0) {
            return modulus(base.remainder(div), exp, div, rem);
        }

        return (modulus(
                base.multiply(base).remainder(div),
                exp.divide(BigInteger.valueOf(2)), div,
                exp.remainder(BigInteger.valueOf(2))
                        .equals(BigInteger.ONE)
                ? base.multiply(rem).remainder(div) : rem));
    }

    public static String encrypt(int msg, Key key) {
        return mod(BigInteger.valueOf(msg), new BigInteger(key.getE()), new BigInteger(key.getProduct())).toString();
    }
    
//    public static String base64Encode(BigInteger originalStr){
//        return Base64.encode(originalStr);
//    }
//    
//    public static String base64Decode(String encodedStr){
//        try {
//            return new BigInteger(Base64.decode(encodedStr)).toString();
//        } catch (Base64DecodingException ex) {
//        }
//        return "0";
//    }
}
