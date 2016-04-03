package Protocols;

import chanels.MC;
import messages.ParseHeader;
import messages.PutChunkMsg;
import peer.Peer;
import listeners.StoreListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Backup extends Thread{

    protected ExecutorService backupService;
    protected Future<Void> storeFuture;

    //sends putchunks on mdb
    //waits for store on ctrl
    public MC mdb;
    protected DatagramPacket send_put_chunk = null;


    byte[] buf;
    byte[] body;
    String filename;
    String fileId;
    int peerId;

    int chunkNo;
    int repDegree;
    public static int currentReplication =0;
    public static String[] peers;
    int numTries = 0;
    int wattingTime = 1000;



    public Backup(int id, String file,String fId , int replication,byte[] b, int cNo,   MC ctrl, MC backup ){
        peerId = id;
        filename = file;
        repDegree = replication;
        chunkNo = cNo;
        peers = new String[100];
        body = b;
        mdb = backup;
        fileId = fId;
        backupService = Executors.newFixedThreadPool(5);
    }

    public void  run(){
        wattingTime = 1000;
        while( numTries < 5 && currentReplication < repDegree ){
            //putchunks on mdb  PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
            PutChunkMsg p = new PutChunkMsg(Peer.version, Peer.id, fileId, chunkNo,body , repDegree);
            byte[] buf = new byte[Peer.datagramWithBodySize] ;
            buf = p.getBytes();
            send_put_chunk = new DatagramPacket(buf , buf.length ,mdb.getMc_addr() , mdb.getMc_port() );
            try {
                numTries++;
                mdb.getMc_socket().send(send_put_chunk);

                storeFuture = backupService.submit(new StoreListener(chunkNo , fileId));

                    try {
                        storeFuture.get(wattingTime, TimeUnit.MILLISECONDS );
                    } catch (TimeoutException e){
                    storeFuture.cancel(true);
                    wattingTime = wattingTime * 2;
                    System.out.println("Terminated!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        backupService.shutdownNow();
    }




    public static synchronized boolean checkSender(String sender){
        boolean exists = false;
        for (int i = 0; i < currentReplication ; i++) {
            if(peers[i].equals(sender))
                exists = true;
        }
        return exists;
    }

    public static synchronized void incrementReplication(){
        currentReplication++;
    }

    public static synchronized void addSender(String senderId){
        peers[currentReplication] = senderId;
    }

}
