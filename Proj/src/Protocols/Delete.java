package Protocols;

import chanels.MC;
import messages.DeleteMsg;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by ei08047 on 23-03-2016.
 */
public class Delete extends Thread {

    MC control;
    String senderId;
    String fileId;
    protected DatagramPacket send_delete = null;

    public Delete(MC ctrl,String sId,String fId){
        control = ctrl;
        senderId = sId;
        fileId = fId;
    }

    public void run(){
        DeleteMsg d = new DeleteMsg(Peer.version, Integer.parseInt(senderId), fileId );
        byte[] buf = new byte[Peer.datagramWithoutBodySize];
        buf = d.getBytes();
        send_delete = new DatagramPacket(buf, buf.length, control.getMc_addr() , control.getMc_port());

        //send enougth times
        //// TODO: 29/03/2016 send delete msg enought times 
        try {
            control.getMc_socket().send(send_delete);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
