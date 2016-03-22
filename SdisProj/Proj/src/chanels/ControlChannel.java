package chanels;

import messages.Stored;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class ControlChannel extends Channel{

    public ControlChannel(String addr, int port) throws IOException {
        super(addr, port);
    }

    //temos de acrescentar o que devolve
    public void receive(){
        byte[] buf = new byte[MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            System.out.println("listening control channel on port: " + mc_port + ", address: " + mc_addr.getHostName());
            mc_socket.receive(packet);
            if(packet.getData() != null){
                String msg = new String(packet.getData());
                System.out.println("received: " + msg);
                //needs parse
               //STORED DELETE REMOVED GETCHUNK
                String[] splitted = msg.split(" ");
                String oper = splitted[0];

                    if (oper.equals("STORED")){
                   // check if rep degree is the requested for that chunk
                        System.out.println("--received a stored");
                }
                else if(oper.equals("DELETE")){
                // After receive, delete all chunks of the specified file
                //no response
                }
                else if(oper.equals("REMOVED")){
                //later
                }else if(oper.equals("GETCHUNK")){
                        // é preciso o peer ver se tem uma copia do chunk , se sim envia pelo mdr
                    }
                else
                    System.out.println("--Not a recognized operation");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
    public DatagramPacket stored(String version, String senderId, String fileId, int chunkNo ){
        Stored msg = new Stored(version, senderId, fileId , chunkNo );
        return  new DatagramPacket(msg.getBytes(), msg.getBytes().length, mc_addr , mc_port);
    }





}
