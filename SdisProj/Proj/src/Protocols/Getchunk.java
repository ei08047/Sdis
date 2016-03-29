package Protocols;

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

    public MulticastSocket mdr,control;

    public Getchunk(MulticastSocket restore,MulticastSocket ctrl){
        mdr = restore;
        control = ctrl;
    }

    public void run(){
        System.out.println("Im here! send me a getchunk!");
        receive();
    }


    //receives getchunk
    public void receive(){
//To avoid flooding the host with CHUNK messages,
// each peer shall wait for a random time uniformly distributed between 0 and 400 ms, before sending the CHUNK message.
// If it receives a CHUNK message before that time expires, it will not send the CHUNK message.
        byte[] buf = new byte[64000];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            control.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("control chanel received : " + msg);
                String[] parsed = msg.split(" ");
                //check if getchunk
                ChunkMsg c = new ChunkMsg(Peer.version,Peer.id, parsed[3], Integer.parseInt(parsed[4]), "body" );
                //prepare chunk / wait
                //send chunk

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
