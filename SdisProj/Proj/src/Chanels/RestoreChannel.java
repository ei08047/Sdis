package chanels;

/**
 * Created by ei08047 on 21-03-2016.
 */

import java.io.IOException;
import java.net.DatagramPacket;


/**
 * Created by ei08047 on 21-03-2016.
 */
public class RestoreChannel extends Channel{

    public RestoreChannel(String addr, int port) throws IOException {
        super(addr, port);
    }

    //temos de acrescentar o que devolve
    public void receive(){
        byte[] buf = new byte[MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            System.out.println("listening restore channel on port: " + mc_port + ", address: " + mc_addr.getHostName());
            mc_socket.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
                String oper = "";
                //needs parse
                if(oper.equals("GETCHUNK")){
                    //case GETCHUNK
                    //send a chunk through control channel
                }
                else{
                    System.out.println("--Not a valid getchunk message");
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //needs method GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>


}
