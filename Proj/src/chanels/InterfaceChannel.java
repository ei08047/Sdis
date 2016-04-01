package chanels;


import Protocols.Backup;
import Protocols.Restore;
import peer.Peer;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class InterfaceChannel extends Thread {
    protected DatagramSocket socket = null;
    protected DatagramPacket recv;
    protected int port;
    byte[] buf;
    static int maxSize = 64000;
    String operation;
    String filename;
    int rep;




    public InterfaceChannel(int port_number) throws IOException {
        //System.out.println("Creating interface connection");
        port = port_number;
        socket = new DatagramSocket(port);
    }

    public void run() {
            System.out.println("Expecting interface commands on port : " + port);
            receive();
    }

    public void receive() {
        //System.out.println("listening on interface channel on port " + port);
        while (true) {
            try {
                buf = new byte[maxSize];
                recv = new DatagramPacket(buf, buf.length);
                socket.receive(recv);
                if(recv.getData() != null){
                    String received = new String(recv.getData(), 0, recv.getLength());
                    //System.out.println("Interface sent : " + received);
                    String[] parse = received.split(" ");
                    operation = parse[0];
                    filename = parse[1]; //first operand must be path to a file!
                    //if file exists
                    ///Just got the command order
                    if(operation.equals("backup")){       // if backup file rep
                        rep = Integer.parseInt(parse[2]);
                        //split
                        File willBeRead = new File ("./data/" +  Peer.id + "/" + filename);
                        int FILE_SIZE = (int) willBeRead.length();

                        int NUMBER_OF_CHUNKS = 0;
                        byte[] temporary = null;

                        InputStream inStream = null;
                        int totalBytesRead = 0;

                        inStream = new BufferedInputStream( (new FileInputStream(willBeRead) ) );

                        while(totalBytesRead < FILE_SIZE){
                            int bytesRemaining = FILE_SIZE - totalBytesRead;
                            if(bytesRemaining < maxSize){
                                maxSize = bytesRemaining;
                            }
                            temporary = new byte[maxSize];
                            int bytesRead = inStream.read(temporary, 0, maxSize);
                            if(bytesRead > 0){
                                //body in temporary
                                //one Backup for each Chunk
                                int index = Peer.getFileIndex(filename);
                                Backup backup = new Backup(Peer.id, Peer.peerFiles[index].getName(), Peer.peerFilesIds[index] ,rep ,"body" ,NUMBER_OF_CHUNKS , Peer.mc, Peer.mdb );
                                backup.start();
                                totalBytesRead += bytesRead;
                                NUMBER_OF_CHUNKS ++;
                            }
                        }
                    }else   //else restore file
                    if(operation.equals("restore")){
                        //// TODO: 01-04-2016  parse restore operation
                        System.out.println("got restore");
                        //get file metadata
                        //first operand must be a file
                        //Restore r = new Restore( filename );
                        //r.start();
                    }else      //delete
                    if(operation.equals("delete")){
                        //// TODO: 01-04-2016 parse delete operation
                        //DELETE <Version> <SenderId> <FileId> <CRLF><CRLF>
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
