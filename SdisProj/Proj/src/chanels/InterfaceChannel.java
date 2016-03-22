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
    String fileId;
    String filename;
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
                    fileId = Peer.getFileId(parse[1]);
                    filename = parse[1];
                    System.out.println("operation: " + operation);
                    ///Just got the command order
                        // if backup file rep
                            //else restore file
                            //delete
                    if(operation.equals("backup")){
                        rep = Integer.parseInt(parse[2]);
                        System.out.println("operation backup started");
                        //first operand must be a file
                        Peer.split(filename);
                        //second must be an int
                        // divide file in chunks
                        // for each send a putchunk message

                        for (int i = 0; i < 105; i++) { ///hardcoded
                            byte[] buf ;
                            String version= "1.0";
                            String senderID = String.valueOf(Peer.id);
                            int chunkNo= i+1;
                            String body = "bla";
                            int repDegree = 1;
                            PutChunk p = new PutChunk(version,senderID,fileId,chunkNo,body,repDegree);
                            buf = p.getHeader().getBytes();

                            InetAddress address = InetAddress.getLocalHost();
                            DatagramPacket d = new DatagramPacket(buf,buf.length , Peer.backup_addr ,Peer.backup_port );
                            Peer.mdb.send(d);
                        }

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
