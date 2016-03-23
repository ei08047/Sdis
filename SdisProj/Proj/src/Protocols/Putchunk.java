package Protocols;

import chanels.MC;
import messages.StoredMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Putchunk extends Thread {
    public MulticastSocket mdb,control;
    DatagramPacket packet_putChunk = null;
    DatagramPacket packet_store = null;

    public Putchunk(MulticastSocket backup , MulticastSocket ctrl ){
        mdb = backup;
        control = ctrl;
    }


    public void run(){
        System.out.println("Im here! send me a putchunk!");
        receive();
    }


    //receives putchunk
    public void receive(){

        byte[] buf = new byte[64000];
        packet_putChunk = new DatagramPacket(buf,buf.length);
        try {
            mdb.receive(packet_putChunk);

            if(packet_putChunk.getData() != null){
                String msg = new String(packet_putChunk.getData());
                System.out.println("received: " + msg);
                // IMP: A peer must never store the chunks of its own files.
                //  IMP: a peer that has stored a chunk must reply with a STORED message to every PUTCHUNK message it receives
                //sends stored
                System.out.println("wait and send store"); // 0 to 400 ms
                //create datagram
                StoredMsg storedMsg = new StoredMsg("version","sender","file", 1);
                buf = storedMsg.getBytes();
                packet_store = new DatagramPacket(buf , buf.length );
                control.send(packet_store);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
