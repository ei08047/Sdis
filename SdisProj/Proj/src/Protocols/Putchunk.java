package Protocols;

import chanels.MC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Putchunk extends Thread {
    public MulticastSocket mdb,control;

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
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            mdb.receive(packet);

            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
                /*
                String oper = "";
                //needs parse
                if(oper.equals("PUTCHUNK")){
                    //case putchunk
                    //send a stored through control channel
                    System.out.println("Do i have this chunk?");
                    System.out.println("I should print a stored here");
                    //Stored( );
                }
                else{
                    System.out.println("--Not a valid putchunk message");
                }
                */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
