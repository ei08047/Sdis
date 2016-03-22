package Protocols;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Getchunk extends Thread {
    //needs mdb channel
    //needs control channel

    public void run(){
        receive();
    }


    //receives getchunk
    public void receive(){
        /*
        byte[] buf = new byte[MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            System.out.println("listening backup channel on port: " + mc_port + ", address: " + mc_addr.getHostName());
            mc_socket.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
    }


    //sends chunks
}
