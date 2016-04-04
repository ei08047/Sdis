package listeners;

import Protocols.Backup;
import chanels.MC;
import messages.ParseHeader;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.Callable;

/**
 * Created by Zé on 03/04/2016.
 */
public class StoreListener implements Callable<Void> {
    public MC control;
    protected DatagramPacket rec_stored = null;
    int chunk;
    String fileId;
    protected byte[] buf;

    public StoreListener(int c , String fId){
        chunk = c;
        fId = fileId;
    }


public Void call() throws InterruptedException, IOException {
    receive();
    return null;
}

//receives store
public void receive() {
    //STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
    buf = new byte[Peer.datagramWithoutBodySize];
    rec_stored = new DatagramPacket(buf, buf.length);
    ParseHeader p = new ParseHeader();

    while (!Thread.interrupted()) {
        try {
            control.getMc_socket().receive(rec_stored);
            if (rec_stored.getData() != null) {
                String msg = new String(rec_stored.getData());
                //System.out.println("control received:  " + msg);
                String[] parsed = p.parse(msg);
                if (parsed[0].equals("STORED")) {
                    //type must be stored
                    if (Integer.parseInt(parsed[4]) == chunk /*&& parsed[3].equals(fileId)*/) {
                        // check if sender is already in sender array
                        if( !Backup.checkSender(parsed[2])){
                            Backup.addSender( parsed[2]);
                            Backup.incrementReplication();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
}

}
