package subProtocols;

import chanels.MC;
import messages.ParseHeader;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

/**
 * Created by zabrn on 03/04/2016.
 */
public class WaitChunk implements Callable<Boolean> {
    private MC restoreChannel;
    private DatagramPacket rec_chunk=null;
    private DatagramPacket waitingChunk=null;
    private int wait;
    private boolean send = false;

    public WaitChunk(int waitingTime, DatagramPacket waitingChunk) {
        restoreChannel = Peer.mdr;
        wait = waitingTime;
        this.waitingChunk = waitingChunk;
    }



    @Override
    public Boolean call() throws Exception {
        while (true) {
            try {
                byte[] buf;
                buf = new byte[Peer.datagramWithoutBodySize];
                rec_chunk = new DatagramPacket(buf, buf.length);
                restoreChannel.getMc_socket().setSoTimeout(wait);
                restoreChannel.getMc_socket().receive(rec_chunk);
                if (rec_chunk.getData() != null) {
                    String msg = new String(rec_chunk.getData());
                    String[] parsed = new ParseHeader().parse(msg);
                    // type must be chunk
                    if(parsed[0].equals("CHUNK")){

                    }
                    if(!rec_chunk.equals(waitingChunk)){
                       return !rec_chunk.equals(waitingChunk);
                    }
                    //// TODO: 29/03/2016 receive chunk save to disk
                    //do i have this chunk
                    // if not save to disk
                }
            }
            catch (SocketTimeoutException e) {

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
