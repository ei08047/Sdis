package peer;

import chanels.*;
import fileOperations.Baina;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Peer {
    int id;
    public static BackupChannel mdb;
    public static ControlChannel mc;
    public static RestoreChannel mdr;

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

                    /*
                    interfaceChannel = new InterfaceChannel(port_number);
                    interfaceChannel.start();
                    mdr = new RestoreChannel("224.0.0.1", 8883);
                    mdb = new BackupChannel("224.0.0.2", 8884);
                    mc = new ControlChannel("224.0.0.3", 8885);
                    mdr.start();
                    mdb.start();
                    mc.start();
                    */
                    Baina b = new Baina("test.pdf");
                }else{
                    System.out.println("error: <port_number>");
                }
            }else{
                System.out.println("error: <host_name>");
            }
        }
    }

}
