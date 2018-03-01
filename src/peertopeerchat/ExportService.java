/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeerchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Biplav
 */
public class ExportService {

    public static void export(List<Person> peerList) {
        try (PrintWriter out = new PrintWriter("peer.lm")) {
            for (Person person : peerList) {
                out.println(((Peer) person).toJson().toString());
            }
        } catch (IOException e) {
            // File writing/opening failed at some stage.
        }
    }
}
