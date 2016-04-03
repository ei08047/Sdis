package listeners;

import chanels.MC;
import messages.ParseHeader;
import messages.StoredMsg;
import peer.Peer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
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
        System.out.println("starting backup listener");
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

                    StoredMsg storedMsg = new StoredMsg(Peer.version, Peer.id, parsed[3], Integer.parseInt(parsed[4]));
                    buf2 = storedMsg.getBytes();
                    packet_store = new DatagramPacket(buf2, buf2.length, control.getMc_addr(), control.getMc_port());

                    if(parsed[0].equals("PUTCHUNK")){ //type must be putchunk

                        if(! (Integer.parseInt(parsed[2]) == Peer.id)){// IMP: A peer must never store the chunks of its own files.
                            //// TODO: 01-04-2016 enhancement
                        /*
                        * A peer should also count the number of confirmation messages for each of the chunks
                         * it has stored and keep that count in non-volatile memory.
                          * This information is used if the peer runs out of disk space: in that event,
                           * the peer will try to free some space by evicting chunks whose actual replication degree is
                            * higher than the desired replication degree.
                            Enhancement: This scheme can deplete the backup space rather rapidly,
                             and cause too much activity on the nodes once that space is full.
                              Can you think of an alternative scheme that ensures the desired replication degree,
                               avoids these problems, and, nevertheless, can interoperate with peers that execute
                               the chunk backup protocol described above?
                        * */

                            String path = "./data/" + Peer.id + "/" + parsed[3];
                            File f = new File(path);
                            File c = new File (path + "/" + parsed[4]);
                            FileWriter fstream = new FileWriter(c.getCanonicalPath());
                            BufferedWriter out = new BufferedWriter(fstream);
                            if (f.exists() && f.isDirectory()) {
                                if(c.exists()){
                                    System.out.println(parsed[4] + " already exists!!");
                                    //create datagram
                                    control.getMc_socket().send(packet_store);
                                }else{
                                    c.getParentFile().mkdirs();
                                    c.createNewFile();
                                    out.write("something");

                                    //save to disk
                                    System.out.println("save to disk");
                                    control.getMc_socket().send(packet_store);
                                }
                            }
                            else
                            {
                                c.getParentFile().mkdirs();
                                c.createNewFile();
                                out.write("something");
                                control.getMc_socket().send(packet_store);
                                System.out.println("just made folder named fileid");
                            }

                        }
                        else
                            continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}
