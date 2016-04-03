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
    int senderId;
    String fileId;
    protected DatagramPacket send_delete = null;

    public Delete(MC ctrl,int sId,String fId){
        control = ctrl;
        senderId = sId;
        fileId = fId;
    }

    public void run(){
        DeleteMsg d = new DeleteMsg(Peer.version,senderId, fileId );
        byte[] buf = new byte[Peer.datagramWithoutBodySize];
        buf = d.getBytes();
        send_delete = new DatagramPacket(buf, buf.length, control.getMc_addr() , control.getMc_port());

        try {
            control.getMc_socket().send(send_delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //send enougth times
        //// TODO: 29/03/2016 send delete msg enought times

        for (int i = 0; i < 5; i++) {
            try {
                control.getMc_socket().send(send_delete);
                sleep(200);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        //// TODO: 01-04-2016 Enhancement

        /*
        * Upon receiving this message, a peer should remove from its backing store all chunks belonging to the specified file.
This message does not elicit any response message.
 An implementation, may send this message as many times as it is deemed necessary to
  ensure that all space used by chunks of the deleted file are deleted in spite of the loss of some messages.
Enhancement: If a peer that backs up some chunks of the file is not running at the time the initiator peer sends a DELETE message
 for that file, the space used by these chunks will never be reclaimed.
  Can you think of a change to the protocol, possibly including additional messages,
   that would allow to reclaim storage space even in that event?
        *
        * */
        try {
            control.getMc_socket().send(send_delete);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
