import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zé on 24/02/2016.
 */
public class Client {

    protected static String host_name; //<host_name> is the name of the host running the server;
    protected static int port_number;//<port_number> is the server port;
    protected static byte[] message;
    protected static String operation;
    protected static String plate;
    protected static String owner;

    //<oper> is either ‘‘register’’or ‘‘lookup’’
    //<opnd>* is the list of arguments
        //<plate number> <owner name>, for register;
        //<plate number>, for lookup.


    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("java Client <host_name> <port_number> <oper> <opnd>*");
            return;
        }else{
            String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
            String patternPortNumber = "^[0-9]{4}$";
            String patternOperation = "^lookup$|^register$";
            String patternPlate = "^[a-zA-Z]{1}[0-9]{2}$|^[0-9]{2}[a-zA-Z]{1}$|^[0-9][a-zA-Z][0-9]$";


            Pattern pattern = Pattern.compile(patternHostName);
            Matcher matcher = pattern.matcher(args[0]);
            if(matcher.matches()){

                host_name = args[0];
                pattern = Pattern.compile(patternPortNumber);
                matcher = pattern.matcher(args[1]);

                if(matcher.matches()){
                    port_number = Integer.parseInt(args[1]);

                    pattern = Pattern.compile(patternOperation);
                    matcher = pattern.matcher(args[2]);

                    if(matcher.matches()){
                       ///operation
                        System.out.println("so far so good");
                        // get a datagram socket
                        DatagramSocket socket = new DatagramSocket();

                        // send request
                        String message = "lookup 71-21-gx";  //lookup case
                        byte[] buf = new byte[256];
                        buf = message.getBytes();
                        System.out.println(message);
                        InetAddress address = InetAddress.getByName(host_name);
                        //System.out.println(address.getHostName());
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_number);
                        socket.send(packet);

                        // get response
                        packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);

                        // display response
                        String received = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("Server responds: " + received);

                        socket.close();


                    }else{
                        System.out.println("error: <oper>");
                    }
                }else{
                    System.out.println("error: <port_number>");
                }
            }else
            {
                System.out.println("error: <host_name>");
            }
        }
    }
}