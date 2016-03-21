package chanels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 21-03-2016.
 */
public abstract class Channel extends Thread {
    protected MulticastSocket mc_socket;
    protected InetAddress mc_addr;
    protected int mc_port;
    protected int MAX_SIZE = 64100;

     public Channel(String addr, int port) throws IOException {
         mc_addr = InetAddress.getByName(addr);
         mc_port = port;
         mc_socket = new MulticastSocket(mc_port);
         mc_socket.joinGroup(mc_addr);
         mc_socket.setTimeToLive(1);
         //mc_socket.setLoopbackMode(true);
    }

    public void run() {
        receive();
    }

    protected abstract void receive();

    public void send(DatagramPacket packet){


        try {
            mc_socket.send(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
