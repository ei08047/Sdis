package Chanels;


import Peer.Peer;
import messages.PutChunk;

import java.io.IOException;
import java.net.*;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class InterfaceChannel extends Thread {
    protected DatagramSocket socket = null;
    protected DatagramPacket recv;
    protected int port;
    byte[] buf;
    static int maxSize = 64000;
     //<IP address>:<port number>
    String operation;

    public InterfaceChannel(int port_number) throws IOException {
        System.out.println("Creating interface connection");
        port = port_number;
        socket = new DatagramSocket(port);
    }

    public void run() {
            System.out.println("Expecting interface commands on port : " + port);
            receive();
    }

    public void receive() {
        System.out.println("listening on interface channel on port " + port);
        while (true) {
            try {
                buf = new byte[maxSize];
                recv = new DatagramPacket(buf, buf.length);
                socket.receive(recv);
                if(recv.getData() != null){
                    String received = new String(recv.getData(), 0, recv.getLength());
                    operation = received;
                    System.out.println("Interface sent : " + operation);
                    System.out.println("datagram size : " + recv.getLength());
                    /*
                    String version= "1.0";
                    String senderID = "ze";
                    String fileID = "zabrn";
                    int chunkNo= 1;
                    String body = "bla";
                    int repDegree = 1;
                    PutChunk p = new PutChunk(version,senderID,fileID,chunkNo,body,repDegree);
                    */
                    Peer.mc.send("zeeeeee");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send() throws IOException{
        //recv = new DatagramPacket(msg.fullmsg.getBytes(), msg.fullmsg.getBytes().length, control_addr, control_port);
        socket.send(recv);
        // System.out.println(msg.fullmsg);
    }
}
