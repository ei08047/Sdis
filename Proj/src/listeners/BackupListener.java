package listeners;

import chanels.MC;
import messages.ParseHeader;
import messages.StoredMsg;
import peer.Peer;
import subProtocols.SendStored;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by ei08047 on 22-03-2016.
 */
//receives putchunk
//sends stored
public class BackupListener extends Thread {

protected ScheduledExecutorService sendStored = Executors.newSingleThreadScheduledExecutor();
public MC mdb;
DatagramPacket packet_putChunk = null;
String fileId;
int chunkNo;

public BackupListener(MC backup ){
    mdb = backup;

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
        packet_putChunk = new DatagramPacket(buf, buf.length);
        try {
            mdb.getMc_socket().receive(packet_putChunk);
            if (packet_putChunk.getData() != null) {
                String msg = new String(packet_putChunk.getData());
                System.out.println("Backup channel received : " + msg);
                ParseHeader p = new ParseHeader();
                String[] parsed = p.parse(msg);
                int sleepTime = rand.nextInt(400) + 1;
                if(parsed[0].equals("PUTCHUNK")){ //type must be putchunk
                    // IMP: A peer must never store the chunks of its own files.
                    if(! (Integer.parseInt(parsed[2]) == Peer.id)){
                        fileId = parsed[3];
                        chunkNo = Integer.parseInt(parsed[4]);
                        //run send stored thread
                        sendStored.schedule( new SendStored(Peer.mc,fileId,chunkNo,"body".getBytes()),sleepTime, TimeUnit.MILLISECONDS);

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
