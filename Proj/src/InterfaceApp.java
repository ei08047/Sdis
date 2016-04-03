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

    protected static byte[] message;
    protected static String construct;
    protected static String operation;
    static InterfaceChannel interfaceChannel;

    static DatagramSocket socket = null;
    static DatagramPacket packet;
    static int port;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("java Interface <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
            return;
        }else{
            InetAddress address = InetAddress.getLocalHost();
            if(isPort(args[0])){
                port = Integer.parseInt(args[0]);
                socket = new DatagramSocket();
                System.out.println("port/InitiationPeer: " + port );
                if(isOperation(args[1])){ ///operation
                    operation = args[1];
                    System.out.println("Operation: " + operation );
                    if(operation.equals("backup")){
                        construct = operation + " " + args[2] + " " + args[3];
                        message = construct.getBytes();
                        // get a datagram socket
                        packet = new DatagramPacket(message, message.length, address, port);
                        // send request
                        socket.send(packet);
                    }
                    else if(operation.equals("restore")){
                        construct = operation + " " + args[2];
                        message = construct.getBytes();
                        // get a datagram socket
                        packet = new DatagramPacket(message, message.length, address, port);
                        // send request
                        socket.send(packet);
                    } else if(operation.equals("delete")){
                        construct = operation + " " + args[2];
                        message = construct.getBytes();
                        // get a datagram socket
                        packet = new DatagramPacket(message, message.length, address, port);
                        // send request
                        socket.send(packet);
                    } else if(operation.equals("reclaim")){
                        //// TODO: 03/04/2016 reclaim 
                    }

                    //socket.close();
                    }else{
                        System.out.println("error: <oper>");
                    }
                }else{
                    System.out.println("error: <port_number>");
                }
            }
        }


    public static boolean isOperation(String oper){
        String patternOperation = "^backup$|^restore$|^delete$";
        Pattern pattern = Pattern.compile(patternOperation);
        Matcher matcher = pattern.matcher(oper);
        return matcher.matches();

    }

    public boolean isFile(String file){return true;}

    public static boolean isPort(String port){
        String patternPortNumber = "^[0-9]{4}$";   // 1024 to 49151
        Pattern pattern = Pattern.compile(patternPortNumber);
        Matcher matcher = pattern.matcher(port);
        return matcher.matches();
    }
}
