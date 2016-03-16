import Chanels.InterfaceChannel;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Peer {
    int id;
    // mc channel
    // mdb channel
    // mdr channel
    //interface channel
    static String host_name;
    static int port_number;
    static InterfaceChannel interfaceChannel;

    public Peer(){
    }

    public static void main(String[] args) throws IOException {
        if(args.length < 1 )
            System.out.println("missing arguments : usage '<IP address>:<port number>' \n");
        else
        {
            String argumnents []=  args[0].split(":");
            String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
            String patternPortNumber = "^[0-9]{4}$";
            Pattern pattern = Pattern.compile(patternHostName);
            Matcher matcher = pattern.matcher(argumnents[0]);
            if(matcher.matches()){
                host_name = args[0];
                pattern = Pattern.compile(patternPortNumber);
                matcher = pattern.matcher(argumnents[1]);

                if(matcher.matches()) {
                    port_number = Integer.parseInt(argumnents[1]);

                    interfaceChannel = new InterfaceChannel(port_number);
                    interfaceChannel.run();
                }else{
                    System.out.println("error: <port_number>");
                }
            }else{
                System.out.println("error: <host_name>");
            }
        }
    }

}
