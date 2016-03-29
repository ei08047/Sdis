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
    String fileId;
    String filename;
    int rep;

    //peer's file metadata
    String[] filenames = null;
    String[] fileIds = null;
    int[] NoChunks = null;


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

                                Backup backup = new Backup(Peer.id, filename,getFileId(filename) , rep ,"body" ,NUMBER_OF_CHUNKS , Peer.mc, Peer.mdb );
                                backup.start();
                                totalBytesRead += bytesRead;
                                NUMBER_OF_CHUNKS ++;
                            }
                        }





                    }else   //else restore file
                    if(operation.equals("restore")){
                        System.out.println("got restore");
                        //get file metadata
                        //first operand must be a file
                        //Restore r = new Restore( filename );
                        //r.start();
                    }else      //delete
                    if(operation.equals("delete")){
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

    public void retrieveMeta(){
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("./data/meta.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int count = 0;
        try {
            while( (strLine = br.readLine()) != null ){
                String[] parsed = strLine.split(" ");
                filenames[count] = parsed[0];
                fileIds[count]= parsed[1];
                NoChunks[count] = Integer.parseInt(parsed[2]);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMeta(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./data/meta.txt"), true));
            for (int i = 0; i < filenames.length; i++) {
                bw.write(filenames[i] + " " + fileIds[i] + " " + NoChunks[i]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // given the filename.extension returns the fileId String
    public static String getFileId(String file){
        File f = new File( "data/" + file );
        String test = file + f.lastModified() ;
        MessageDigest digest = null;
        String result;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(test.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            result = hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return result;
    }

}
