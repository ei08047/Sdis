package peer;

import fileManager.Database;
import listeners.ControlListener;
import listeners.BackupListener;
import chanels.*;

import java.io.*;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Peer {

    //
    public static int id;
    public static String version = "1.0";

    public static String path = "./data/";
    public static String databasePath = "./db/";

    public static Database db = new Database();

    public static File[] peerFiles;
    public static String[] peerFilesIds = new String[100];
    public static int[] peerFilesNoChunks = new int[100];
    public static int nFiles = 0;

    public static int datagramWithBodySize = 65000;
    public static int datagramWithoutBodySize = 1000;
    public static MC mc,mdb,mdr;

    public static String control_addr;
    public static String backup_addr;
    public static String restore_addr;

    public static int control_port;
    public static int backup_port;
    public static int restore_port;



    public static BackupListener backup_listener;
    public static ControlListener controlListener;



    static InterfaceChannel interfaceChannel;

    public Peer() {
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

            if (isMulticastAddress(control[0]) && isMulticastAddress(backup[0]) && isMulticastAddress(restore[0]) ) {

                control_addr = control[0];
                backup_addr = backup[0];
                restore_addr = restore[0];

                if (isPortNumber(control[1]) && isPortNumber(backup[1]) && isPortNumber(restore[1]) ) {
                    control_port = Integer.parseInt(control[1]);
                    backup_port = Integer.parseInt(backup[1]);
                    restore_port = Integer.parseInt(restore[1]);
                    try{ //
                        //create dir data if it doesnt exist
                        new File(path + id).mkdirs();
                        new File(databasePath).mkdirs();
                    }catch (SecurityException e ){
                        System.out.println(e.getMessage());
                    }

                    try {
                        db.load();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    peerFiles = retrieveFiles();

                    for (int i = 0; i < nFiles; i++) {
                        peerFilesIds[i] = getFileId(peerFiles[i].getName());
                        peerFilesNoChunks[i] =  getFileNoChunks(peerFiles[i]);
                        System.out.println( "file:  " + peerFiles[i].getName() + " | | " +  peerFilesIds[i] + " | | " +  peerFilesNoChunks[i] );
                    }

                    //mc,mdb,mdr
                    mc = new MC(control_addr, control_port, "control");
                    mdb = new MC(backup_addr, backup_port, "backup");
                    mdr = new MC(restore_addr, restore_port, "restore");


                    interfaceChannel = new InterfaceChannel(id);
                    interfaceChannel.start();

                    backup_listener = new BackupListener(mdb);
                    controlListener = new ControlListener( mdr , mc );

                    backup_listener.start();
                    controlListener.start();

                } else {
                    System.out.println("error: <port_number>");
                }
            } else {
                System.out.println("error: <host_name>");
            }
        }
    }

    public static File[] retrieveFiles(){
        File folder = new File("./data/" + Peer.id);
        File[] listOfFiles = folder.listFiles();
        File[] filesWithoutDirs = new File[100];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                filesWithoutDirs[nFiles] = listOfFiles[i];
                nFiles++;
            }
        }
        return filesWithoutDirs;
    }



    // given the filename.extension returns the fileId String
    public static String getFileId(String file){
        File f = new File( "data/" + file );
        String test = file + f.lastModified() ;
        MessageDigest digest = null;
        String result;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(test.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            result = hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return result;
    }

    public static int getFileNoChunks(File f){
        if( f.length() < 64000)
            return 1;
        else
            return ((int)f.length() / 64000) + 1 ;
    }

    public static int getFileIndex(String name){
        int ret = 0;
        for (int i = 0; i < nFiles; i++) {
            if(peerFiles[i].getName().equals(name))
                ret = i;
        }
        return  ret;
    }

    public static boolean isMulticastAddress(String addr){
        String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
        Pattern pattern = Pattern.compile(patternHostName);
        Matcher matcher = pattern.matcher(addr);
        return matcher.matches();
    }

    public static boolean isPortNumber(String port){
        String patternPortNumber = "^[0-9]{4}$";
        Pattern pattern = Pattern.compile(patternPortNumber);
        Matcher matcher = pattern.matcher(port);
        return matcher.matches();
    }





}


