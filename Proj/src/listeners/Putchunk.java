package listeners;

import chanels.MC;
import messages.ParseHeader;
import messages.StoredMsg;
import peer.Peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Random;

/**
 * Created by ei08047 on 22-03-2016.
 */
//receives putchunk
//sends stored
public class Putchunk extends Thread {
    public MC mdb,control;
    DatagramPacket packet_putChunk = null;
    DatagramPacket packet_store = null;

    public Putchunk(MC backup , MC ctrl ){
        mdb = backup;
        control = ctrl;
    }

    public void run(){
        receive();
    }


    //receives putchunk
    public void receive(){
        Random rand = new Random();
        while (true) {

            byte[] buf = new byte[Peer.datagramWithBodySize];
            byte[] buf2 = new byte[Peer.datagramWithoutBodySize];
            packet_putChunk = new DatagramPacket(buf, buf.length);
            try {
                mdb.getMc_socket().receive(packet_putChunk);
                if (packet_putChunk.getData() != null) {
                    String msg = new String(packet_putChunk.getData());
                    System.out.println("Backup channel received : " + msg);
                    ParseHeader p = new ParseHeader();
                    String[] parsed = p.parse(msg);
                    int sleepTime = rand.nextInt(400) + 1;
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //// TODO: 29/03/2016 verifications
                    // IMP: A peer must never store the chunks of its own files.
                    //  IMP: a peer that has stored a chunk must reply with a STORED message to every PUTCHUNK message it receives
                    // 1 - find peer directory
                    // 1.1 - See if this peer has file in question
                    //    2 - find / create directory with name = fileId
                    //       3 - find / create file named chunkNo
                    //          4 - sends stored
                    StoredMsg storedMsg = new StoredMsg(Peer.version, Peer.id, parsed[3], Integer.parseInt(parsed[4]));
                    buf2 = storedMsg.getBytes();
                    packet_store = new DatagramPacket(buf2, buf2.length, control.getMc_addr(), control.getMc_port());

                    if(parsed[0].equals("PUTCHUNK")){ //type must be putchunk
                        String path = "./data/" + Peer.id + "/" + parsed[3];
                        File f = new File(path);
                        File c = new File (path + "/" + parsed[4]);
                        if (f.exists() && f.isDirectory()) {
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
                        }
                        else
                        {
                            c.getParentFile().mkdirs();
                            c.createNewFile();
                            control.getMc_socket().send(packet_store);
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
