/**
 * Created by Zé on 24/02/2016.
 */
/**
 * Created by Zé on 24/02/2016.
 */

import sun.java2d.SurfaceDataProxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.Pipe;


public class ServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    protected String operation;
    protected String plate;
    protected String name;

    protected String srvc_addr;
    protected int srvc_port;

    final static String mcast_addr = "224.0.0.3";
    final static int mcast_port = 8888;
    /*
    * <srvc_port> is the port number where the server provides the service
    * <mcast_addr> is the IP address of the multicast group used by the server to advertise its service.
    * <mcast_port> is the multicast group port number used by the server to advertise its service
    * */


    public ServerThread(String[] args) throws IOException {

        this.srvc_port = Integer.parseInt(args[0]);
        socket = new DatagramSocket(srvc_port);
    }


    public void run() {

        // Get the address that we are going to connect to.

        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(mcast_addr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Open a new DatagramSocket, which will be used to send the data.
        InetAddress ip;
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            ip = InetAddress.getLocalHost();

            srvc_addr = ip.getHostAddress();
            for (int i = 0; i < 5; i++) {

                String msg = srvc_port + " " + srvc_addr;

                // Create a packet that will contain the data

                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),

                        msg.getBytes().length, addr, mcast_port);

                serverSocket.send(msgPacket);
                System.out.println("multicast: " + mcast_addr + "  "  + mcast_port + "  " + srvc_addr + "  " + srvc_port );
                System.out.println("Server sent packet with msg: " + msg);

                Thread.sleep(500);

            }

        } catch (IOException ex) {

            ex.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
