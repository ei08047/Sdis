package Protocols;

import chanels.MC;
import messages.ParseHeader;
import messages.StoredMsg;
import peer.Peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Putchunk extends Thread {
    public MC mdb,control;
    DatagramPacket packet_putChunk = null;
    DatagramPacket packet_store = null;

    public Putchunk(MC backup , MC ctrl ){
        mdb = backup;
        control = ctrl;
    }


    public void run(){
        System.out.println("Im here! send me a putchunk!");
        receive();
    }


    //receives putchunk
    public void receive(){

        while (true) {

            byte[] buf = new byte[64000];
            packet_putChunk = new DatagramPacket(buf, buf.length);
            try {
                mdb.getMc_socket().receive(packet_putChunk);

                if (packet_putChunk.getData() != null) {
                    String msg = new String(packet_putChunk.getData());
                    System.out.println("received222: " + msg);
                    ParseHeader p = new ParseHeader();
                    String[] parsed = p.parse(msg);

                    // IMP: A peer must never store the chunks of its own files.
                    //  IMP: a peer that has stored a chunk must reply with a STORED message to every PUTCHUNK message it receives
                    // 1 - find peer directory
                    // 1.1 - See if this peer has file in question
                    //    2 - find / create directory with name = fileId
                    //       3 - find / create file named chunkNo
                    //          4 - sends stored
                    System.out.println("wait and send store"); // 0 to 400 ms
                    StoredMsg storedMsg = new StoredMsg(Peer.version, Peer.id, parsed[3], 1);
                    buf = storedMsg.getBytes();
                    packet_store = new DatagramPacket(buf, buf.length, control.getMc_addr(), control.getMc_port());

                    if(parsed[0].equals("PUTCHUNK")){ //type must be putchunk
                        String path = "./data/" + Peer.id + "/" + parsed[3];
                        File f = new File(path);
                        if (f.exists() && f.isDirectory()) {
                            File c = new File (path + "/" + parsed[4]);
                            if(c.exists()){
                                System.out.println(parsed[4] + " already exists!!");
                                //create datagram
                                control.getMc_socket().send(packet_store);
                            }else{
                                c.getParentFile().mkdirs();
                                c.createNewFile();
                                //save to disk
                                System.out.println("save to disk");
                                control.getMc_socket().send(packet_store);
                            }
                        }else
                        {
                            f.mkdir();
                            System.out.println("just made folder named fileid");
                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}
