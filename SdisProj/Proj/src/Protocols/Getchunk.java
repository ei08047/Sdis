package Protocols;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Getchunk extends Thread {

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

        byte[] buf = new byte[64000];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            //System.out.println("listening backup channel on port: " + mc_port + ", address: " + mc_addr.getHostName());
            control.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //sends chunks
}
