/**
 * Created by Zé on 24/02/2016.
 */
/**
 * Created by Zé on 24/02/2016.
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected int port;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    protected String operation;
    protected String plate;
    protected String name;

    public ServerThread(String[] args) throws IOException {
        System.out.println(args[0]);
        this.port = Integer.parseInt(args[0]);
        socket = new DatagramSocket(port);
    }


    public void run() {

        while (moreQuotes) {
            try {
                System.out.println("try..");
                byte[] buf = new byte[256];
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                if (packet.getData() != null) {
                    byte[] buffer = packet.getData();
                    String message = new String(buffer);
                    String[] elements = message.split(" ");
                    if(elements.length <1)
                    {
                        System.out.println("ooops");
                    }else{
                    operation = elements[0];
                    plate = elements[1];
                    System.out.println("operation " + operation + "  " + "plate  " + plate);
                    String dString = null;
                    dString = lookup(plate.trim());


                    buf = dString.getBytes();
                    // send the response to the client at "address" and "port"
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    packet = new DatagramPacket(buf, buf.length, address, port);

                    socket.send(packet);}

                }
            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }

    protected String lookup(String plate) {
        String returnValue = null;
        try {
            in = new BufferedReader(new FileReader("plates-record.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while((returnValue = in.readLine()) != null){
                String[] splited = returnValue.split(" ");
                System.out.println("line:" + splited[0].getBytes().length + "||splited:" + plate.getBytes().length + "||");

                if( ( plate.equals(splited[0])) ){
                    System.out.println("found it");
                    String full_name = "";
                    for (int i = 1; i < splited.length; i++) {
                        full_name += splited[i];
                    }
                    return full_name;
                }else{
                    moreQuotes = false;
                    returnValue = "No more quotes. Goodbye.";
                    return returnValue;
                }
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
}
