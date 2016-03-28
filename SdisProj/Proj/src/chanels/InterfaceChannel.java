package chanels;


import Protocols.Backup;
import peer.Peer;

import java.io.*;
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
                    ///Just got the command order
                    if(operation.equals("backup")){       // if backup file rep
                        System.out.println("operation: " + operation);
                        filename = parse[1]; //first operand must be path to a file!
                        //if file exists
                        //fileId = Peer.getFileId(parse[1]); //get fileId
                        rep = Integer.parseInt(parse[2]);
                        //split
                        File willBeRead = new File ("./data/" +  Peer.id + "/" + filename);
                        int FILE_SIZE = (int) willBeRead.length();
                        System.out.println("Total File Size: "+FILE_SIZE);

                        int NUMBER_OF_CHUNKS = 0;
                        byte[] temporary = null;

                        InputStream inStream = null;
                        int totalBytesRead = 0;

                        inStream = new BufferedInputStream( (new FileInputStream(willBeRead) ) );

                        while(totalBytesRead < FILE_SIZE){
                            int bytesRemaining = FILE_SIZE - totalBytesRead;
                            if(bytesRemaining < maxSize){
                                maxSize = bytesRemaining;
                                System.out.println("CHUNK_SIZE: "+maxSize);
                            }
                            temporary = new byte[maxSize];
                            int bytesRead = inStream.read(temporary, 0, maxSize);
                            if(bytesRead > 0){
                                //body in temporary
                                //one Backup for each Chunk
                                Backup backup = new Backup(Peer.id, "file", rep ,"body" ,NUMBER_OF_CHUNKS , Peer.mc, Peer.mdb );
                                backup.start();
                                totalBytesRead += bytesRead;
                                NUMBER_OF_CHUNKS ++;
                            }
                        }
                    }else   //else restore file
                    if(operation.equals("restore")){
                        //Restore()
                        //first operand must be a file
                    }else      //delete
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
