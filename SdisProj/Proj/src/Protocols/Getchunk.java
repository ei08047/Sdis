package Protocols;

import chanels.MC;
import messages.ChunkMsg;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 22-03-2016.
 */


public class Getchunk extends Thread {

    //receive getchunk on mc
    //sends chunks on mdr

    public MC mdr,control;

    DatagramPacket packet_getChunk = null;
    DatagramPacket packet_chunk = null;

    public Getchunk(MC restore, MC ctrl){
        mdr = restore;
        control = ctrl;
    }

    public void run(){
        System.out.println("Im here! send me a getchunk!");
        receive();
    }


    //receives getchunk
    public void receive(){
    byte[] buf = new byte[Peer.datagramWithoutBodySize];
    packet_getChunk = new DatagramPacket(buf,buf.length);
    try {
        control.getMc_socket().receive(packet_getChunk);
        if(packet_getChunk.getData() != null){
            String msg = new String(packet_getChunk.getData());
            System.out.println("control chanel received : " + msg);
            String[] parsed = msg.split(" ");
            //check if getchunk
            if(parsed[0].equals("GETCHUNK")){
                ChunkMsg c = new ChunkMsg(Peer.version,Peer.id, parsed[3], Integer.parseInt(parsed[4]), "body" );
                byte[] buf2 = new byte[Peer.datagramWithBodySize];
                buf2 = c.getBytes();
                packet_chunk = new DatagramPacket(buf2, buf2.length, mdr.getMc_addr(), mdr.getMc_port());
                //// TODO: 29/03/2016  wait and listen before send chunk
                //To avoid flooding the host with CHUNK messages,
                // each peer shall wait for a random time uniformly distributed between 0 and 400 ms, before sending the CHUNK message.
                // If it receives a CHUNK message before that time expires, it will not send the CHUNK message.
                //prepare chunk / wait
                //send chunk
                //listens for store for some time
                mdr.getMc_socket().send(packet_chunk);
            }
            else{
                if(parsed[0].equals("DELETE")){
                    //// TODO: 29/03/2016  delete backup chunks
                }
                else
                {
                    if(parsed[0].equals("REMOVED")){
                    //// TODO: 29/03/2016 on removed update
                    /*
                    * Upon receiving this message, a peer that has a local copy of the chunk
                     * shall update its local count of this chunk.
                     * If this count drops below the desired replication degree of that chunk,
                      * it shall initiate the chunk backup subprotocol
                      * after a random delay uniformly distributed between 0 and 400 ms.
                      * If during this delay, a peer receives a PUTCHUNK message for the same file
                       * chunk, it should back off and restrain from starting
                        * yet another backup subprotocol for that file chunk.
                    * */
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
