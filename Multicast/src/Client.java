 import java.io.IOException;

        import java.net.DatagramPacket;

        import java.net.InetAddress;
        import java.net.MulticastSocket;
        import java.net.UnknownHostException;

public class Client {

    protected static String mcast_addr ;
    protected static int mcast_port ;
    protected static String srvc_addr;
    protected static int srvc_port ;
    protected static String oper;

    public static void main(String[] args) throws UnknownHostException {

        if(args.length < 4){
            System.out.println("java client <mcast_addr> <mcast_port> <oper> <opnd> * ");
        }
        else{
            mcast_addr = args[0];
            mcast_port = Integer.parseInt(args[1]);
            oper = args[2];
        }
        // Get the address that we are going to connect to.

        InetAddress address = InetAddress.getByName(mcast_addr);

        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.

        byte[] buf = new byte[256];


        // Create a new Multicast socket (that will allow other sockets/programs

        // to join it as well.

        try (MulticastSocket clientSocket = new MulticastSocket(mcast_port)){

            //Joint the Multicast group.

            clientSocket.joinGroup(address);

            while (true) {

                // Receive the information and print it.

                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);

                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Socket 1 received msg: " + msg);
                String [] splitted = msg.split(" ");
                srvc_port = Integer.parseInt( splitted[0]);
                srvc_addr = splitted[1];
                System.out.println("multicast: " + mcast_addr + " " + mcast_port + " " + srvc_addr + " " + srvc_port );

                //prepare request here
            }

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }

}
