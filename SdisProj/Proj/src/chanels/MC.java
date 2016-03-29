package chanels;

import java.io.IOException;
import java.net.*;


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
    protected int MAX_SIZE= 65536;

    public MC(String addr, int port, String type) {
        this.type = type;

        try {
            mc_addr = InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mc_port = port;
        try {
            mc_socket = new MulticastSocket(mc_port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mc_socket.joinGroup(mc_addr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mc_socket.setTimeToLive(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mc_socket.setLoopbackMode(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }

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

    public DatagramPacket receive()
    {
        byte[] buffer = new byte[MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
        return packet;

    }

    public void send(String msg) throws IOException {
        DatagramPacket packet = new DatagramPacket(msg.trim().getBytes(), msg.trim().getBytes().length, mc_addr, mc_port);
        mc_socket.send(packet);
    }


}

