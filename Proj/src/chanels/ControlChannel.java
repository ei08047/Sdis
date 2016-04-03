package chanels;

import Protocols.DeleteThread;
import Protocols.WaitChunk;
import fileManager.FileManager;
import fileManager.Record;
import messages.ChunkMsg;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zabrn on 03/04/2016.
 */
public class ControlChannel extends Thread{

    ExecutorService executor;

    public ControlChannel() {
    executor = Executors.newFixedThreadPool(10);
    }

    public void run(){
        System.out.println("Im here! send me a getchunk!");
        receive();
    }


    //receives getchunk
    public void receive(){

        byte[] buf = new byte[Peer.datagramWithoutBodySize];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            try {

                Peer.mc.getMc_socket().receive(packet);

                if (packet.getData() != null) {

                    String msg = new String(packet.getData());

                    System.out.println("control chanel received : " + msg);

                    String[] parsed = msg.split(" ");
                    String type = parsed[0];
                    String fileID = parsed[3];
                    int chunkNo = Integer.parseInt(parsed[4]);


                    //check if getchunk
                    if (type.equals("GETCHUNK")) {

                        byte[] body = new byte[64000];

                        ChunkMsg chunk = new ChunkMsg(Peer.version, Peer.id, fileID , chunkNo, body);
                        boolean state = false;
                        byte[] buf2 = new byte[Peer.datagramWithBodySize];
                        buf2 = chunk.getBytes();
                        packet = new DatagramPacket(buf2, buf2.length, Peer.mdr.getMc_addr(), Peer.mdr.getMc_port());

                        int sleepingTime=0;
                        WaitChunk wait = new WaitChunk(sleepingTime, packet);
                        try {
                            wait.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("aqui");
                        }


                        //send chunk
                        //// TODO: 29/03/2016  wait and listen before send chunk
                        //To avoid flooding the host with CHUNK messages,
                        // each peer shall wait for a random time uniformly distributed between 0 and 400 ms, before sending the CHUNK message.
                        // If it receives a CHUNK message before that time expires, it will not send the CHUNK message.
                        //prepare chunk / wait
                        //send chunk
                        //listens for store for some time
                        Peer.mdr.getMc_socket().send(packet);
                    } else {
                        if (type.equals("DELETE")) { //0-oper 1-version 2-sender 3-file
                            executor.execute(new DeleteThread(fileID));
                            //enhancment

                        } else {
                            if (parsed[0].equals("REMOVED")) {
                                //// TODO: 29/03/2016 on removed update
                        /*
                        * Upon receiving this message, a peer that has a local copy of the chunk
                         * shall update its local count of this chunk.
                         * If this count drops below the desired replication degree of that chunk,
                          * it shall initiate the chunk backup subprotocol
                          * after a random delay uniformly distributed between 0 and 400 ms.
                          * If during this delay, a peer receives a PUTCHUNK message for the same file
                           * chunk, it should back off and restrain from starting
                            * yet another backup subprotocol for that file chunk.
                        * */

                                //todo update own files replication degree
                            /*
                            * Upon receiving this message, a peer that has a local copy of the chunk shall update its
                            * local count of this chunk.
                            * If this count drops below the desired replication degree of that chunk,
                             * it shall initiate the chunk backup subprotocol after a random delay uniformly
                             * distributed between 0 and 400 ms.
                             * If during this delay, a peer receives a PUTCHUNK message for the same file chunk,
                              * it should back off and restrain from starting yet another backup subprotocol for that file chunk.
                            * */
                            } else {
                                if (parsed[0].equals("STORED")) {
                                    Record r = new Record(parsed[3], Integer.parseInt(parsed[2]), Integer.parseInt(parsed[4]));

                                    if(!Peer.db.exists(r))
                                    {
                                        Peer.db.addRecord(r);
                                        r.save();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
