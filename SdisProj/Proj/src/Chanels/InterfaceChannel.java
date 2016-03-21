package chanels;


import peer.Peer;
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
    String file;
    int rep;

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
                    System.out.println("Interface sent : " + received);
                    System.out.println("datagram size : " + recv.getLength());
                    String[] parse = received.split(" ");
                    operation = parse[0];
                    file = parse[1];
                    System.out.println("operation: " + operation);
                    ///Just got the command order
                        // if backup file rep
                            //else restore file
                            //delete
                    if(operation.equals("backup")){
                        rep = Integer.parseInt(parse[2]);
                        System.out.println("operation backup started");
                        //first operand must be a file
                        //second must be an int
                        // divide file in chunks
                        // for each send a putchunk message
                    /*
                    String version= "1.0";
                    String senderID = "ze";
                    String fileID = "zabrn";
                    int chunkNo= 1;
                    String body = "bla";
                    int repDegree = 1;
                    PutChunk p = new PutChunk(version,senderID,fileID,chunkNo,body,repDegree);
                    Peer.mc.send(p.getBytes());  // this should be sent via mdb channel
                    */
                    }else
                    if(operation.equals("restore")){
                        //first operand must be a file
                    }else
                    if(operation.equals("delete")){
                        //first operand must be a file

                    }

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
