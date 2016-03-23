import chanels.InterfaceChannel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class InterfaceApp {
    int peer;
    String sub_protocol;
    String opnd_1;
    String opnd_2;
    protected static byte[] message;
    protected static String construct;
    protected static String operation;
    static InterfaceChannel interfaceChannel;

    static DatagramSocket socket = null;
    static int port;

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("java Interface <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
            return;
        }else{
            String patternPortNumber = "^[0-9]{4}$";
            String patternOperation = "^backup$|^restore$|^deletion$";

            Pattern pattern = Pattern.compile(patternPortNumber);
            Matcher matcher = pattern.matcher(args[0]);
            socket = new DatagramSocket(port);
            if(matcher.matches()){

                port = Integer.parseInt(args[0]);
                pattern = Pattern.compile(patternOperation);
                matcher = pattern.matcher(args[1]);

                if(matcher.matches()){
                    operation = args[1];
                    ///operation
                    byte[] buf ;
                    construct = operation + " " + args[2] + " " + args[3];
                    buf = construct.getBytes();
                    InetAddress address = InetAddress.getLocalHost();
                    System.out.println("port/InitiationPeer: " + port );
                    // get a datagram socket
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                    // send request
                    socket.send(packet);
                    //socket.close();
                    }else{
                        System.out.println("error: <oper>");
                    }
                }else{
                    System.out.println("error: <port_number>");
                }
            }
        }

}
