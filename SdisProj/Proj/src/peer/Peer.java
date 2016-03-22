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
public class Peer {
    public static int id;

    public static String path = "data/";
    public static String[] directories;

    public static BackupChannel mdb;
    public static ControlChannel mc;
    public static RestoreChannel mdr;

    public static InetAddress control_addr;
    public static InetAddress backup_addr;
    public static InetAddress restore_addr;

    public static int control_port;
    public static int backup_port;
    public static int restore_port;


    static InterfaceChannel interfaceChannel;

    public Peer(){
    }

//8888 239.254.254.3:8887 239.254.254.4:8886 239.254.254.5:8885

    public static void main(String[] args) throws IOException {
        if(args.length < 4 )
            System.out.println("missing arguments : usage '<ServerID> <control address>:<port number> <backup address>:<port number> <restore address>:<port number>' \n");
        else
        {
            retrieveDirectories();

            id = Integer.parseInt(args[0]);

            String control []=  args[1].split(":");
            String backup []=  args[2].split(":");
            String restore []=  args[3].split(":");

            String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
            String patternPortNumber = "^[0-9]{4}$";
            Pattern pattern = Pattern.compile(patternHostName);
            Matcher matcher1 = pattern.matcher(control[0]);
            Matcher matcher2 = pattern.matcher(backup[0]);
            Matcher matcher3 = pattern.matcher(restore[0]);
            if(matcher1.matches() && matcher2.matches() && matcher3.matches()){

                control_addr = InetAddress.getByName(control[0]);
                backup_addr = InetAddress.getByName(backup[0]);
                restore_addr = InetAddress.getByName( restore[0]);

                pattern = Pattern.compile(patternPortNumber);
                matcher1 = pattern.matcher(control[1]);
                matcher2 = pattern.matcher(backup[1]);
                matcher3 = pattern.matcher(restore[1]);

                if(matcher1.matches() && matcher2.matches() && matcher3.matches()) {

                    control_port = Integer.parseInt(control[1]);
                    backup_port = Integer.parseInt(backup[1]);
                    restore_port = Integer.parseInt(restore[1]);

                    interfaceChannel = new InterfaceChannel(id);
                    interfaceChannel.start();


                    mdr = new RestoreChannel(restore[0], restore_port);
                    mdb = new BackupChannel(backup[0], backup_port);
                    mc = new ControlChannel(control[0], control_port);
                    mdr.start();
                    mdb.start();
                    mc.start();


                }else{
                    System.out.println("error: <port_number>");
                }
            }else{
                System.out.println("error: <host_name>");
            }
        }
    }


    public static void retrieveDirectories(){
        File file = new File(path);
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        System.out.println(Arrays.toString(directories));
    }



    public static String getFileId(String file){
        File f = new File( path + file );
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
    } // given the filename.extension returns the fileId String


    public static void split(String filename) throws FileNotFoundException {
        int partCounter = 1;
        int sizeOfFiles = 64000;
        byte[] buffer = new byte[sizeOfFiles];
        String fileId = getFileId(filename);
        new File("data/" + fileId).mkdir(); // create Dir
        File f = new File(path + filename);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
        int tmp = 0;
        try {
            while ((tmp = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                File newFile = new File("data/" + fileId,
                        String.format("%03d", partCounter++));
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, tmp);//tmp is chunk size
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfDirExists(String fileId){
        boolean ret = false;
        for (int i = 0; i < directories.length ; i++) {
            if(fileId.equals(directories[i]))
                ret = true;
        }
        return ret;
    }


    public static void saveChunk(String fileId, int chunkNo, byte[] body){
        File z = new File("data/"+fileId);
        if(!z.exists()){
            new File("data/" + fileId).mkdir(); // create Dir
        }

        byte data[] = body;
        Path file = Paths.get("data/" + fileId + "/" + chunkNo);
        try {
            Files.write(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
