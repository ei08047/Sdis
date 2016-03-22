package peer;

import chanels.*;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Peer2 {
    public static int id;

    public static String path = "data/";
    public static String[] directories;

    public static MC mc,mdb,mdr;

    public static String control_addr;
    public static String backup_addr;
    public static String restore_addr;

    public static int control_port;
    public static int backup_port;
    public static int restore_port;


    static InterfaceChannel interfaceChannel;

    public Peer2() {
    }

//8888 239.254.254.3:8887 239.254.254.4:8886 239.254.254.5:8885

    public static void main(String[] args) throws IOException {
        if (args.length < 4)
            System.out.println("missing arguments : usage '<ServerID> <control address>:<port number> <backup address>:<port number> <restore address>:<port number>' \n");
        else {

            id = Integer.parseInt(args[0]);

            String control[] = args[1].split(":");
            String backup[] = args[2].split(":");
            String restore[] = args[3].split(":");

            String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
            String patternPortNumber = "^[0-9]{4}$";
            Pattern pattern = Pattern.compile(patternHostName);
            Matcher matcher1 = pattern.matcher(control[0]);
            Matcher matcher2 = pattern.matcher(backup[0]);
            Matcher matcher3 = pattern.matcher(restore[0]);
            if (matcher1.matches() && matcher2.matches() && matcher3.matches()) {

                control_addr = control[0];
                backup_addr = backup[0];
                restore_addr = restore[0];

                pattern = Pattern.compile(patternPortNumber);
                matcher1 = pattern.matcher(control[1]);
                matcher2 = pattern.matcher(backup[1]);
                matcher3 = pattern.matcher(restore[1]);

                if (matcher1.matches() && matcher2.matches() && matcher3.matches()) {

                    control_port = Integer.parseInt(control[1]);
                    backup_port = Integer.parseInt(backup[1]);
                    restore_port = Integer.parseInt(restore[1]);

                    interfaceChannel = new InterfaceChannel(id);
                    interfaceChannel.start();

                    //mc,mdb,mdr
                    mc = new MC(control_addr, control_port);
                    mdb = new MC(backup_addr, backup_port);
                    mdr = new MC(restore_addr, restore_port);

                    //cria putchunk thread
                    // cria chunk



                } else {
                    System.out.println("error: <port_number>");
                }
            } else {
                System.out.println("error: <host_name>");
            }
        }
    }
}


