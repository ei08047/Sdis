package Protocols;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Restore extends Thread{

    //sends getchunks on mc
    //recv chunk on mdr

    public MulticastSocket control,mdr;

    protected DatagramPacket recv;
    protected int port;



    byte[] buf;
    static int maxSize = 64000;

    String fileId;
    int chunkNo;
    int peerId;
    String filename;


    public Restore(int id, String file ){
        peerId = id;
        filename = file;
        //fileId = getFileId(filename);
    }

    public void  run(){
        System.out.println("operation restore started");
        //get file metadata

        //sends getchunk
        receive(); //for some time
    }


    //receives chunk
    public void receive() {
        while (true) {
            try {
                buf = new byte[maxSize];
                recv = new DatagramPacket(buf, buf.length);
                mdr.receive(recv);
                if (recv.getData() != null) {
                    //do i have this chunk
                    // if not save to disk
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
