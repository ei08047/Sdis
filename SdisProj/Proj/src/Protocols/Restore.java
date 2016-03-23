package Protocols;

import chanels.MC;
import messages.PutChunk;
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
        fileId = getFileId(filename);
    }

    public void  run(){
        System.out.println("operation restore started");
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

    // given the filename.extension returns the fileId String
    public static String getFileId(String file){
        File f = new File( "data/" + file );
        String test = file + f.lastModified() ;
        MessageDigest digest = null;
        String result;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(test.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            result = hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return result;
    }


}
