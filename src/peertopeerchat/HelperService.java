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
public class HelperService {

    public static String toAscii(String orig) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < orig.length(); i++) {
            char c = orig.charAt(i);
            if (c != ' ') {
                str.append((int) c);
            }
            str.append(' ');
        }
        return str.toString();
    }

    public static String asciiToNormal(String asciiString) {
        StringBuilder msg = new StringBuilder();
        StringBuilder token = new StringBuilder();
        char prevChar = ' ';
        char thisChar;
        for (int i = 0; i < asciiString.length(); i++) {
            thisChar = asciiString.charAt(i);
            if (thisChar == ' ') {
                if (prevChar == ' ') {
                    msg.append(' ');
                } else {
                    msg.append((char) Integer.parseInt(token.toString()));
                    token.delete(0, token.length());
                }
            } else {
                token.append(thisChar);
            }
            prevChar = thisChar;
        }
        return msg.toString();
    }
}
