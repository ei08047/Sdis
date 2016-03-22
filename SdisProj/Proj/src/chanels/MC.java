package chanels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 21-03-2016.
 */
public  class MC {
    protected MulticastSocket mc_socket;
    protected InetAddress mc_addr;
    protected int mc_port;

    public MC(String addr, int port) throws IOException {
        mc_addr = InetAddress.getByName(addr);
        mc_port = port;
        mc_socket = new MulticastSocket(mc_port);
        mc_socket.joinGroup(mc_addr);
        mc_socket.setTimeToLive(1);
        //mc_socket.setLoopbackMode(true);
    }

    public MulticastSocket getMc_socket(){
        return mc_socket;
    }

    public int getMc_port(){
        return mc_port;
    }

    public String getMc_addr(){
        return mc_addr.getHostAddress();
    }
}

