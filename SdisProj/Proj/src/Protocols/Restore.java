package Protocols;

import chanels.MC;
import messages.PutChunk;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Restore extends Thread{

    protected DatagramSocket socket = null;
    protected DatagramPacket recv;
    protected int port;


    public MC control;
    //needs interface channel
    //waits for backup message
    //waits for store s
    byte[] buf;
    static int maxSize = 64000;

    String fileId;


    int peerId;
    String filename;


    public Restore(int id, String file ){
        peerId = id;
        filename = file;

    }

    public void  run(){
        System.out.println("operation backup started");
        receive();
    }


    //receives chunk
    public void receive() {
        System.out.println("listening on control channel on port " + port);
        while (true) {
            try {
                buf = new byte[maxSize];
                recv = new DatagramPacket(buf, buf.length);
                socket.receive(recv);
                if (recv.getData() != null) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //sends getchunk



}
