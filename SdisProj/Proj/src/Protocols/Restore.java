package Protocols;

import chanels.MC;
import messages.GetChunkMsg;
import peer.Peer;

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

    public MC control,mdr;

    protected DatagramPacket rec_chunk = null;
    protected DatagramPacket send_get_chunk = null;


    byte[] buf;
    static int maxSize = 64000;

    String fileId;
    String senderId;
    int chunkNo;


    public Restore( String sender, String fId, int cNo, MC ctrl, MC restore ){
        senderId = sender;
        fileId = fId;
        chunkNo = cNo;
        control = ctrl;
        mdr = restore;
    }

    public void  run(){
        System.out.println("operation restore started");
        GetChunkMsg g = new GetChunkMsg(Peer.version, Peer.id, fileId, chunkNo);
        byte[] buf = new byte[64000] ;
        buf = g.getBytes();
        send_get_chunk = new DatagramPacket(buf , buf.length ,control.getMc_addr() , control.getMc_port() );

        try {
            control.getMc_socket().send(send_get_chunk);
            //sends getchunk
            receive(); //for some time
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //receives chunk
    public void receive() {
        while (true) {
            try {
                buf = new byte[maxSize];
                rec_chunk = new DatagramPacket(buf, buf.length);
                mdr.getMc_socket().receive(rec_chunk);
                if (rec_chunk.getData() != null) {
                    // type must be chunk
                    //do i have this chunk
                    // if not save to disk
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
