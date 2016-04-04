package subProtocols;

import chanels.MC;
import messages.StoredMsg;
import peer.Peer;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Created by Zé on 04/04/2016.
 */
public class SendStored implements Runnable {
    public MC control;
    DatagramPacket packet_store = null;
    byte[] buf2 = new byte[Peer.datagramWithoutBodySize];

    String fileId;
    int chunkNo;
    byte[] chunk;

    public SendStored(MC ctrl, String fId, int cNo, byte[] content){
        control = ctrl;
        fileId = fId;
        chunkNo = cNo;
        chunk = content;
    }


    @Override
    public void run() {

        StoredMsg storedMsg = new StoredMsg(Peer.version, Peer.id, fileId , chunkNo);
        buf2 = storedMsg.getBytes();
        packet_store = new DatagramPacket(buf2, buf2.length, control.getMc_addr(), control.getMc_port());

        String path = "./data/" + Peer.id + "/" + fileId;

        File c = new File (path + "/" + chunkNo);

            if(c.exists()){
                //create datagram
                try {
                    control.getMc_socket().send(packet_store);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    c.createNewFile();
                    FileOutputStream stream = new FileOutputStream(path);
                    stream.write(chunk);
                    stream.close();
                    control.getMc_socket().send(packet_store);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }


    }
}
