package chanels;


import peer.Peer;

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
                    String[] parse = received.split(" ");
                    operation = parse[0];
                    System.out.println("operation: " + operation);
                    ///Just got the command order
                        // if backup file rep
                            //else restore file
                            //delete
                    if(operation.equals("backup")){


                        filename = parse[1]; //first operand must be path to a file!
                        //if file exists
                        fileId = Peer.getFileId(parse[1]); //get fileId
                        rep = Integer.parseInt(parse[2]);
                        //split
                        //one Backup for each Chunk
                        //Backup backup = new Backup(int id, String file, int replication )
                    }else
                    if(operation.equals("restore")){
                        //Restore()
                        //first operand must be a file
                    }else
                    if(operation.equals("delete")){
                        //Delete()
                        //first operand must be a file
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
