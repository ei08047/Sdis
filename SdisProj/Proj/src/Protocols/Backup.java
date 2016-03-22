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
public class Backup extends Thread{

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
    int repDegree;
    //fileId
    //repDegree
    //numChunks
    //putchunk
    //receiveStored

    public Backup(int id, String file, int replication ){
        peerId = id;
        filename = file;
        repDegree = replication;
    }

    public void  run(){
        System.out.println("operation backup started");
        receive();
    }


    //receives store
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


    //sends putchunk



}
