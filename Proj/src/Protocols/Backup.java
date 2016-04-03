package Protocols;

import chanels.MC;
import messages.ParseHeader;
import messages.PutChunkMsg;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
    byte[] body;
    String filename;
    String fileId;
    int peerId;

    int chunkNo;
    int repDegree;
    int currentReplication =0;
    String[] peers;
    int numTries = 0;
    int wattingTime = 1000;



    public Backup(int id, String file,String fId , int replication,byte[] b, int cNo,   MC ctrl, MC backup ){
        peerId = id;
        filename = file;
        repDegree = replication;
        chunkNo = cNo;
        peers = new String[100];
        body = b;
        control = ctrl;
        mdb = backup;
        fileId = fId;
    }

    public void  run(){
        wattingTime = 1000;
        while( numTries < 5 && currentReplication < repDegree ){ //peers.length < repDegree &&
            //putchunks on mdb  PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
            PutChunkMsg p = new PutChunkMsg(Peer.version, Peer.id, fileId, chunkNo,body , repDegree);
            byte[] buf = new byte[Peer.datagramWithBodySize] ;
            buf = p.getBytes();
            send_put_chunk = new DatagramPacket(buf , buf.length ,mdb.getMc_addr() , mdb.getMc_port() );

            try {
                numTries++;
                mdb.getMc_socket().send(send_put_chunk);
                control.getMc_socket().setSoTimeout(wattingTime);
                while(true){
                    receive();
                }
            } catch (SocketTimeoutException e) {
                wattingTime = wattingTime * 2;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //receives store
    public void receive() throws IOException {
        //STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        ParseHeader p = new ParseHeader();

        buf = new byte[Peer.datagramWithoutBodySize];
        rec_stored = new DatagramPacket(buf, buf.length);
        control.getMc_socket().receive(rec_stored);
        if (rec_stored.getData() != null) {
            String msg = new String(rec_stored.getData());
            //System.out.println("control received:  " + msg);
            String[] parsed = p.parse(msg);
            if(parsed[0].equals("STORED")){
                //type must be stored
                if(Integer.parseInt(parsed[4]) == chunkNo /*&& parsed[3].equals(fileId)*/ ){
                    // check if sender is already in sender array
                    if( !checkSender(parsed[2])){
                        peers[currentReplication] = parsed[2];
                        currentReplication ++ ;
                    }
                }
            }
        }


    }



    public boolean checkSender(String sender){
        boolean exists = false;
        for (int i = 0; i < currentReplication ; i++) {
            if(peers[i].equals(sender))
                exists = true;
        }
        return exists;
    }

}
