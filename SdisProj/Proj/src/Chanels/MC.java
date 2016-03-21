package Chanels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class MC extends Thread{
    private MulticastSocket mc_socket;
    private InetAddress mc_addr;
    private int mc_port;

    public MC(int port, String ip) throws IOException {
        mc_addr = InetAddress.getByName(ip);
        mc_port = port;
        mc_socket = new MulticastSocket(mc_port);
        mc_socket.joinGroup(mc_addr);
    }

    public void run() {
        System.out.println("creating control connection");
        receive();
    }

    public void receive(){
        byte[] buf = new byte[64100];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            System.out.println("listening mc channel on port: " + mc_port + ", address: " + mc_addr.getHostName());
            mc_socket.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(String msg){

        System.out.println("about to send message on mc channel");
        DatagramPacket packet = new DatagramPacket(msg.trim().getBytes(), msg.trim().length(), mc_addr , mc_port);
        try {
            mc_socket.send(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
