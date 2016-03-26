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
    protected String type;

    public MC(String addr, int port, String type) throws IOException {
        this.type = type;
        mc_addr = InetAddress.getByName(addr);
        mc_port = port;
        mc_socket = new MulticastSocket(mc_port);
        mc_socket.joinGroup(mc_addr);
        mc_socket.setTimeToLive(1);
        mc_socket.setLoopbackMode(true);

        //System.out.println("channel :" + type + "    on addr: " + mc_addr.getHostName() + "  port:" + mc_port);
    }

    public MulticastSocket getMc_socket(){
        return mc_socket;
    }

    public int getMc_port(){
        return mc_port;
    }

    public InetAddress getMc_addr(){
        return mc_addr;
    }
}

