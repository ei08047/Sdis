package Protocols;

import chanels.MC;
import messages.PutChunkMsg;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Backup extends Thread{

    //sends putchunks on mdb
    //waits for store on ctrl

    public MC control,mdb;
    protected DatagramPacket send_put_chunk = null;
    protected DatagramPacket rec_stored = null;

    byte[] buf;
    static int maxSize = 64000;

    String fileId;


    int peerId;
    String filename;
    int repDegree;
    int currentReplication =0;
    String[] peers;
    int numTries = 0;



    public Backup(int id, String file, int replication, MC ctrl, MC backup ){
        peerId = id;
        filename = file;
        repDegree = replication;
        peers = new String[repDegree];
        control = ctrl;
        mdb = backup;
        fileId = getFileId(filename);
    }

    public void  run(){
        System.out.println("operation backup started");
        while( numTries < 5 || currentReplication < repDegree ){ //peers.length < repDegree &&
            //putchunks on mdb
            PutChunkMsg p = new PutChunkMsg("version", "sender", "file", 1, "body" , 1);
            byte[] buf = new byte[64000] ;
            buf = p.getBytes();
            send_put_chunk = new DatagramPacket(buf , buf.length ,mdb.getMc_addr() , mdb.getMc_port() );
            try {
                numTries++;
                System.out.println("num Tries: " + numTries);
               mdb.getMc_socket().send(send_put_chunk);
                //try to
                receive();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }



    //receives store
    public void receive() {
        //STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        //replace true with timer
        while (true) {
            try {
                buf = new byte[maxSize];
                rec_stored = new DatagramPacket(buf, buf.length);
                control.getMc_socket().receive(rec_stored);
                if (rec_stored.getData() != null) {
                    //type must be stored
                    // add senderId to peers array
                    peers[currentReplication] = rec_stored.getAddress().getHostAddress();
                    currentReplication ++ ;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }


    //sends putchunk
    public void send(){
        /*
        * This message is used to ensure that the chunk is backed up with the desired replication degree as follows.
         * The initiator-peer collects the confirmation messages during a time interval of one second.
         * If the number of confirmation messages it received up to the end of that interval is lower
         * than the desired replication degree, it retransmits the backup message on the MDB channel,
         * and doubles the time interval for receiving confirmation messages.
          * This procedure is repeated up to a maximum number of five times,
          * i.e.the initiator will send at most 5 PUTCHUNK messages per chunk.
        * */
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
